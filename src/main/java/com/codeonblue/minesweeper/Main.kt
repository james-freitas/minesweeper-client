package com.codeonblue.minesweeper

import java.util.Scanner


fun main(args: Array<String>) {

    println("Welcome to Minesweeper API client - Type \"exit\" to quit")
    println("-----------------------------------------------------------")
    println()
    println("Enter the url of MineSweeper API [default: minesweep-api.herokuapp.com] > ")
    val scan = Scanner(System.`in`)
    var path = scan.nextLine().trim()
    if (path.isEmpty()) {
        path = "https://minesweep-api.herokuapp.com"
    }
    println(path)
}
