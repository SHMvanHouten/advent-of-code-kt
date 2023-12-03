package com.github.shmvanhouten.adventofcode2023.day03

import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.WEST
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun findPartsNumbers(input: String): List<Int> {
    val grid = charGrid(input)
    val symbols = grid.coordinatesMatching { !it.isDigit() && it != '.' }
    return getFullNumbersAdjacentToSymbols(grid, symbols)
}

fun getFullNumbersAdjacentToSymbols(grid: Grid<Char>, symbols: List<Coordinate>): List<Int> {
    return numbersAdjacentToSymbolsWithLocations(grid, symbols)
        .map { it.map { it.second }.joinToString("").toInt() }
}

private fun numbersAdjacentToSymbolsWithLocations(
    grid: Grid<Char>,
    symbols: List<Coordinate>
) = grid.coordinatesMatching { it.isDigit() }
    .filter { hasNoNrToTheLeft(it, grid) }
    .map { joinWithDigitsToTheRight(it, grid) }
    .filter { isAdjacentToSymbol(it, symbols) }

fun isAdjacentToSymbol(numberWithLocations: List<Pair<Coordinate, Char>>, symbols: List<Coordinate>): Boolean {
    return numberWithLocations.map { it.first }
        .any { digitLoc -> digitLoc.getSurrounding().any { symbols.contains(it) } }
}

fun joinWithDigitsToTheRight(loc: Coordinate, grid: Grid<Char>): List<Pair<Coordinate, Char>> {
    val digits = mutableListOf(loc to grid[loc])
    var nextDigit = getNextDigitToTheRight(loc, grid)
    while (nextDigit != null) {
        digits += nextDigit to grid[nextDigit]
        nextDigit = getNextDigitToTheRight(nextDigit, grid)
    }
    return digits
}

fun getNextDigitToTheRight(loc: Coordinate, grid: Grid<Char>): Coordinate? {
    val nextLoc = loc.move(EAST)
    return if (grid.contains(nextLoc) && grid[nextLoc].isDigit()) nextLoc
    else null
}

fun hasNoNrToTheLeft(loc: Coordinate, grid: Grid<Char>): Boolean {
    return !grid.contains(loc.move(WEST)) || !grid[loc.move(WEST)].isDigit()
}

fun getGearRatios(input: String): List<Long> {
    val grid = charGrid(input)
    return grid.coordinatesMatching { it == '*' }
        .map { findNumbersNextTo(it, grid) }
        .filter { it.size == 2 }
        .map { it.product() }
}

fun findNumbersNextTo(candidateGear: Coordinate, grid: Grid<Char>): List<Int> {
    return getFullNumbersAdjacentToSymbols(grid, listOf(candidateGear))
}
