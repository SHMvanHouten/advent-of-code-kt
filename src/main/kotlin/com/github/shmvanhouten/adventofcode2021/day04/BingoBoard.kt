package com.github.shmvanhouten.adventofcode2021.day04

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

data class BingoBoard(val cells: Map<Long, Cell>) {
    private val cellsByCoordinate = cells.values.associateBy { it.location }
    fun flip(number: Long) {
        cells[number]?.flip()
    }

    fun hasBingo(): Boolean {
        val cellSizeRange = 0..cells.values.map { it.location }.maxOf { it.x }
        return cellSizeRange
            .any {
                hasFullRow(it, cellSizeRange) || hasFullColumn(it, cellSizeRange)
            }
    }

    private fun hasFullRow(y: Int, cellSizeRange: IntRange): Boolean {
        return cellSizeRange.all{ x -> cellsByCoordinate[Coordinate(x, y)]!!.isMarked }
    }

    private fun hasFullColumn(x: Int, cellSizeRange: IntRange): Boolean {
        return cellSizeRange.all { y -> cellsByCoordinate[Coordinate(x,y)]!!.isMarked }
    }

    fun score(winningNumber: Long): Long {
        return winningNumber * sumOfAllUnmarkedNumbers()
    }

    private fun sumOfAllUnmarkedNumbers() = cells.values.filter { !it.isMarked }.sumOf { it.number }
}

data class Cell(val location: Coordinate, val number: Long, var isMarked: Boolean) {
    fun flip() {
        isMarked = true
    }
}
