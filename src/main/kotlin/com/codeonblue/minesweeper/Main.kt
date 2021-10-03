package com.codeonblue.minesweeper

fun main(args: Array<String>) {

    // Prints the id of the new game created
    val gameId = createGameAndReturnGameId()
    println("Game id of the game created: $gameId")
    println()

    // Prints all cells that could be reveled and the game status
    val reveledCellResponse = revealCellOn(
        gameId = gameId,
        cellNumber = "1"
    )
    println("Reveled cells")
    println(reveledCellResponse.reveledCells.toString())
    println()
    println("Game status: ${reveledCellResponse.gameStatus}")
    println()

    // Prints the status of the cell marked
    val currentCellStatus = markCellAndReturnCurrentCellStatus(
        gameId = gameId,
        cellNumber = "1"
    )
    println("Cell 1 has the $currentCellStatus status")
}