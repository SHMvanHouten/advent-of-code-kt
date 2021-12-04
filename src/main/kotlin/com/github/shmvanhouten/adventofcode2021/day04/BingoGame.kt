package com.github.shmvanhouten.adventofcode2021.day04

class BingoGame(val boards: List<BingoBoard>) {
    fun findWinner(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        winningNumbers.forEach { winningNumber ->
            boards.forEach { it.mark(winningNumber) }
            boards.find { it.hasBingo() }
                ?.let { return it to winningNumber }
        }
        error("winning bingo-board not found")
    }

    fun findLoser(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        val remainingBoards = boards.toMutableList()
        winningNumbers.forEach { winningNumber ->
            remainingBoards.forEach { it.mark(winningNumber) }

            val winners = remainingBoards.filter { it.hasBingo() }
            remainingBoards -= winners

            if (remainingBoards.isEmpty()) return winners.last() to winningNumber
        }
        error("losing bingo-board not found")
    }

}