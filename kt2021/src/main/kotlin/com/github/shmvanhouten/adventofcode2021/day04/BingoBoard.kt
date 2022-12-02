package com.github.shmvanhouten.adventofcode2021.day04

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

data class BingoBoard(val cells: Map<Long, Cell>) {
    private val cellsByCoordinate = cells.values.associateBy { it.location }
    private val rangeOfCellSizes: IntRange = 0..cells.values.map { it.location }.maxOf { it.x }

    fun mark(number: Long) {
        cells[number]?.mark()
    }

    fun hasBingo(): Boolean {
        return rangeOfCellSizes
            .any {hasFullRow(it) || hasFullColumn(it)}
    }

    fun score(winningNumber: Long): Long {
        return winningNumber * sumOfAllUnmarkedNumbers()
    }

    private fun hasFullRow(y: Int): Boolean =
        rangeOfCellSizes.all{ x -> cellsByCoordinate[Coordinate(x, y)]!!.isMarked }

    private fun hasFullColumn(x: Int): Boolean =
        rangeOfCellSizes.all { y -> cellsByCoordinate[Coordinate(x,y)]!!.isMarked }

    private fun sumOfAllUnmarkedNumbers() =
        cells.values.filter { !it.isMarked }.sumOf { it.number }
}

data class Cell(val location: Coordinate, val number: Long, var isMarked: Boolean) {
    fun mark() {
        isMarked = true
    }
}
