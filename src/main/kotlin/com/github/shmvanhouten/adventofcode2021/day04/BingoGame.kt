package com.github.shmvanhouten.adventofcode2021.day04

class BingoGame(val boards: List<BingoBoard>) {
    fun findWinner(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        var i = 0
        while (boards.none { it.hasBingo() }) {
            val winningNumber = winningNumbers[i]
            boards.forEach { it.flip(winningNumber) }
            val winner = boards.find { it.hasBingo() }
            winner?.let { return it to winningNumber}
            i++
        }
        error("winning bingoboard not found")
    }
}