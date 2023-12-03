package com.github.shmvanhouten.adventofcode2023.day03

import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.WEST
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun findPartsNumbers(input: String): List<Int> {
    val grid = charGrid(input)
    return listFullNumbersWithLocations(grid)
        .filter { isAdjacentToSymbol(it, grid) }
        .map { it.value }
}

fun findGearRatios(input: String): List<Long> {
    val grid = charGrid(input)
    val numbers = listFullNumbersWithLocations(grid)
    return grid.coordinatesMatching { it == '*' }
        .map { numbers.adjacentTo(it) }
        .filter { it.size == 2 }
        .map { it.map(NumberWithLocations::value) }
        .map { it.product() }
}

private fun listFullNumbersWithLocations(grid: Grid<Char>): List<NumberWithLocations> =
    grid.coordinatesMatching { it.isDigit() }
        .filter { hasNoDigitToTheLeft(it, grid) }
        .map { joinWithDigitsToTheRight(it, grid) }

private fun isAdjacentToSymbol(numberWithLocations: NumberWithLocations, grid: Grid<Char>): Boolean =
    numberWithLocations.locations
        .any { loc -> loc.getSurrounding().any { grid.getOrNull(it)?.isSymbol()?:false } }

private fun joinWithDigitsToTheRight(loc: Coordinate, grid: Grid<Char>): NumberWithLocations {
    val digits = generateSequence(loc) { it.move(EAST) }
        .map { it to (grid.getOrNull(it)?:'x') }
        .takeWhile { (_, c) -> c.isDigit() }

    return NumberWithLocations(
        digits.map { it.second }.joinToString("").toInt(),
        digits.map { it.first }.toList()
    )
}

private fun hasNoDigitToTheLeft(loc: Coordinate, grid: Grid<Char>): Boolean {
    return !grid.contains(loc.move(WEST)) || !grid[loc.move(WEST)].isDigit()
}

private fun Char.isSymbol() = !this.isDigit() && this != '.'

private fun List<NumberWithLocations>.adjacentTo(location: Coordinate): List<NumberWithLocations> =
    filter{ it.isAdjacentTo(location) }

data class NumberWithLocations(val value: Int, val locations: List<Coordinate>) {
    fun isAdjacentTo(location: Coordinate): Boolean =
        location.getSurrounding().any { locations.contains(it) }
}