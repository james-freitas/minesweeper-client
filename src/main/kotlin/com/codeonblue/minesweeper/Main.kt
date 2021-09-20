package com.codeonblue.minesweeper

import com.codeonblue.minesweeper.client.MineSweeperClient
import java.util.Scanner
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    // Prints the id of the new game created
    println(createGameAndReturnGameId())

    // Prints all cells that could be revealed
    println(revealCellOn(
        gameId = "1",
        cellNumber = "1"
    ))

    // Prints the status of the cell marked
    println(markCellAndReturnCurrentCellStatus(
        gameId = "a723dbce-eaa3-498f-9887-57ca1d33bd44",
        cellNumber = "1",
        cellCurrentStatus = "UNCHECKED"
    ))
}