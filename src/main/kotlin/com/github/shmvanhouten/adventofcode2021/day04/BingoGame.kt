package com.github.shmvanhouten.adventofcode2021.day04

class BingoGame(val boards: List<BingoBoard>) {
    fun findWinner(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        winningNumbers.forEach { winningNumber ->
            boards.forEach { it.flip(winningNumber) }
            val winner = boards.find { it.hasBingo() }
            winner?.let { return it to winningNumber}
        }
        error("winning bingoboard not found")
    }

    fun findLoser(winningNumbers: List<Long>): Pair<BingoBoard, Long> {
        val remainingBoards = boards.toMutableList()
        winningNumbers.forEach { winningNumber ->
            remainingBoards.forEach { it.flip(winningNumber) }
            val winner = removeWinnersFromTheBoard(remainingBoards)
            if(remainingBoards.isEmpty()) {
                return winner!! to winningNumber
            }
        }
        error("losing bingoboard not found")
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