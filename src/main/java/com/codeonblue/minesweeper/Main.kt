package com.codeonblue.minesweeper

import com.codeonblue.minesweeper.dto.CreatedGameResponse
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

    println("Program exited")
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
    val (_, _, result) = "/games"
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
                println("Program exited")
            }
            return ""
        }
    )
}

inline fun <reified T> generateResponseBody(responseData: ByteArray): T {
    return jacksonObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(responseData)
}

fun testApi() {

    val scan = Scanner(System.`in`)
    while (true) {
        val input = scan.nextLine()
        if (input.trim().toLowerCase() == "exit") {
            break
        }
    }

    // TODO: Create new game
    // TODO: Mark a cell in the created game
    // TODO: Reveal a cell

}


private fun printHeaders() {
    println("Welcome to Minesweeper API client - Type \"exit\" to quit")
    println("-----------------------------------------------------------")
    println()
    println("Base url of MineSweeper API > minesweep-api.herokuapp.com")
    println()
}
