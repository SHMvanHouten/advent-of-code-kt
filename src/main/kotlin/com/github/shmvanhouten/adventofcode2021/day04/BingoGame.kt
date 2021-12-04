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

    fun findLoser(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        var i = 0
        val remainingBoards = boards.toMutableList()
        while (true) {
            val winningNumber = winningNumbers[i]
            remainingBoards.forEach { it.flip(winningNumber) }
            val winner = removeWinnersFromTheBoard(remainingBoards)
            if(remainingBoards.isEmpty()) {
                return winner!! to winningNumber
            }
            i++
        }
    }

    private fun removeWinnersFromTheBoard(remainingBoards: MutableList<BingoBoard>): BingoBoard? {
        val winners = remainingBoards.filter { it.hasBingo() }
        if(winners.isNotEmpty()) {
            val winner = remainingBoards.last()
            remainingBoards -= winners
            return winner
        } else {
            return null
        }
    }
}