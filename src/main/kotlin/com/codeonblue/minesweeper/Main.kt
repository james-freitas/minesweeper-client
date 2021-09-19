package com.codeonblue.minesweeper

import com.codeonblue.minesweeper.client.MineSweeperClient
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
            "2" -> println(revealCell(gameId))
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
        println("Mark a cell - Enter cell current status(UNCHECKED | FLAGGED | QUESTION_MARK: ")
        val cellCurrentStatus = scan.nextLine().trim().toUpperCase()

        val client = MineSweeperClient(BASE_URL)
        return client.markCellAndReturnCurrentCellStatus(
            gameId = gameId,
            cellNumber = cellNumber,
            cellCurrentStatus = cellCurrentStatus
        )

    } catch (ex: Exception) {
        println("An error occurred due to: ${ex.message}")
        exitProcess(0)
    }
}

fun revealCell(gameId: String): Map<String, Int> {
    val scan = Scanner(System.`in`)

    try {
        println("Mark a cell - Enter cell number: ")
        val cellNumber = scan.nextLine().trim().toUpperCase()

        val client = MineSweeperClient(BASE_URL)
        return client.revealCell(
            gameId = gameId,
            cellNumber = cellNumber
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

private fun getGameId() = MineSweeperClient(BASE_URL).getGameId()

private fun printHeaders() {
    println("Welcome to Minesweeper API client - Type \"exit\" to quit")
    println("-----------------------------------------------------------")
    println()
    println("Base url of MineSweeper API > minesweep-api.herokuapp.com")
    println()
}
