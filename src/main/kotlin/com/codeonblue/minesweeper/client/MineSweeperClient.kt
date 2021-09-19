package com.codeonblue.minesweeper.client

import com.codeonblue.minesweeper.dto.CellStatus
import com.codeonblue.minesweeper.dto.CreatedGameResponse
import com.codeonblue.minesweeper.dto.MarkCellDto
import com.codeonblue.minesweeper.dto.RevealedCellResponse
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import kotlin.system.exitProcess

class MineSweeperClient(private val baseUrl: String? = "https://minesweep-api.herokuapp.com") {

    fun getGameId(): String {
        FuelManager.instance.basePath = baseUrl
        val (request, _, result) = "/games"
            .httpPost()
            .response()

        result.fold(
            success = { responseData ->
                val gameResponse: CreatedGameResponse = generateResponseBody(responseData)
                return gameResponse.gameId
            },
            failure = {
                if (it.response.statusCode != 201) {
                    println("Could not create a new game due to API communication issues.")
                    println("Request sent: $request")
                    println("Program exited")
                    exitProcess(0)
                }
                return ""
            }
        )
    }

    fun markCellAndReturnCurrentCellStatus(
        gameId: String,
        cellNumber: String,
        cellCurrentStatus: String
    ): String {
        FuelManager.instance.basePath = baseUrl
        val(request, _, result) = "/games/$gameId/cells/$cellNumber/mark"
            .httpPost()
            .header("Content-Type" to "application/json")
            .body(
                generateRequestBody(
                    MarkCellDto(CellStatus.valueOf(cellCurrentStatus))
                )
            )
            .response()
        result.fold(
            success = { responseData ->
                val markCellResponse: MarkCellDto = generateResponseBody(responseData)
                return markCellResponse.cellCurrentStatus.name
            },
            failure = {
                if (it.response.statusCode != 200) {
                    println("Fail to mark cell due to API communication issues.")
                    println("Request sent: $request")
                    println("------------------------------")
                    println("Response code: ${it.response.statusCode}")
                    println("Response details: ${it.response}")
                    println()
                    println("Program exited due to API error")
                    exitProcess(0)
                }
                return ""
            }
        )
    }

    private fun generateRequestBody(requestBody: Any): String {
        return jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
            .writeValueAsString(requestBody)
    }

    private inline fun <reified T> generateResponseBody(responseData: ByteArray): T {
        return jacksonObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .readValue(responseData)
    }

    fun revealCell(gameId: String, cellNumber: String): Map<String, Int> {
        FuelManager.instance.basePath = baseUrl
        val(request, _, result) = "/games/$gameId/cells/$cellNumber/reveal"
            .httpPost()
            .header("Content-Type" to "application/json")
            .response()
        result.fold(
            success = { responseData ->
                val revealedCell: RevealedCellResponse = generateResponseBody(responseData)
                return revealedCell.revealedCells
            },
            failure = {
                if (it.response.statusCode != 200) {
                    println("Fail to mark cell due to API communication issues.")
                    println("Request sent: $request")
                    println("------------------------------")
                    println("Response code: ${it.response.statusCode}")
                    println("Response details: ${it.response}")
                    println()
                    println("Program exited due to API error")
                    exitProcess(0)
                }
                return emptyMap()
            }
        )
    }
}
