package com.github.shmvanhouten.adventofcode2024.day04

import com.github.shmvanhouten.adventofcode.utility.collections.countDuplicates
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun findXMasses(input: String): Int {
    return charGrid(input).withIndex()
        .filter { it.item == 'X' }
        .sumOf { countXMases(it.location, charGrid(input)) }
}

fun findCrossMasses(input: String): Int {
    return charGrid(input).withIndex()
        .filter { it.item == 'M' }
        .flatMap { countCrossMasses(it.location, charGrid(input)) }
        .countDuplicates()
}

private fun countXMases(location: Coordinate, grid: Grid<Char>): Int {
    return RelativePosition.entries
        .count { grid.readInDirection(location, it, "XMAS") }
}

private fun countCrossMasses(location: Coordinate, grid: Grid<Char>): List<Coordinate> {
    return listOf(
        RelativePosition.NORTH_EAST,
        RelativePosition.NORTH_WEST,
        RelativePosition.SOUTH_EAST,
        RelativePosition.SOUTH_WEST,
    )
        .filter { grid.readInDirection(location, it, "MAS") }
        .map { location.move(it, 1) }
}

private fun Grid<Char>.readInDirection(location: Coordinate, direction: RelativePosition, word: String): Boolean {
    return word.withIndex().all { (i, c) ->
        this.getOrNull(location.move(direction, i)) == c
    }
}
