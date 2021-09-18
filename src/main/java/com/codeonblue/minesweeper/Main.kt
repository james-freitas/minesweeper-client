package com.codeonblue.minesweeper

import com.codeonblue.minesweeper.dto.CellStatus
import com.codeonblue.minesweeper.dto.CreatedGameResponse
import com.codeonblue.minesweeper.dto.MarkCellDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import java.util.Scanner
import kotlin.system.exitProcess


const val BASE_URL = "https://minesweep-api.herokuapp.com"

fun main(args: Array<String>) {

    printHeaders()

    inviteToPlay()

    val gameId = getGameId()
    println("Id of game created: $gameId")

    playGame(gameId)

    println("Program exited")
}

fun playGame(gameId: String) {

    println("Options")
    println("-----------------------------------------------------------")
    println()
    println("1. Mark a cell")
    println("2. Reveal a cell")
    println("3. End game")
    println()

    val scan = Scanner(System.`in`)
    while (true) {
        val input = scan.nextLine()
        when (input.trim().toLowerCase()) {
            "1" -> println(markCell(gameId))
            "2" -> println("revealCell")
            "3" -> {
                println("You asked to exit the game")
                exitProcess(0)
            }
        }
    }
}

fun markCell(gameId: String): String {
    val scan = Scanner(System.`in`)

    try {
        println("Mark a cell - Enter cell number: ")
        val cellNumber = scan.nextLine().trim().toUpperCase()
        println("Mark a cell - Enter cell current status(UNCHECKED|FLAGGED|CHECKED|QUESTION_MARK: ")
        val cellCurrentStatus = scan.nextLine().trim().toUpperCase()

        FuelManager.instance.basePath = BASE_URL
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

    } catch (ex: Exception) {
        println("An error occurred due to: ${ex.message}")
        exitProcess(0)
    }
}

private fun inviteToPlay() {
    println("Do you want to create a new game (yes/no)? ")

    val gameScan = Scanner(System.`in`)
    val gameAnswer = gameScan.nextLine()
    if (gameAnswer.trim().toLowerCase() != "yes") {
        println("You opted to leave.  See you next time")
        exitProcess(0)
    }
}

fun getGameId(): String {
    FuelManager.instance.basePath = BASE_URL
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

fun generateRequestBody(requestBody: Any): String {
    return jacksonObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
        .writeValueAsString(requestBody)
}

inline fun <reified T> generateResponseBody(responseData: ByteArray): T {
    return jacksonObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(responseData)
}

private fun printHeaders() {
    println("Welcome to Minesweeper API client - Type \"exit\" to quit")
    println("-----------------------------------------------------------")
    println()
    println("Base url of MineSweeper API > minesweep-api.herokuapp.com")
    println()
}
