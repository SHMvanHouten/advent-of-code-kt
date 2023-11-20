package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromCoordinates

const val DROPLET = '#'
const val UNVISITED = '.'
const val OPEN_AIR = ' '

fun countExposedSides(_g: Grid<Char>): Long {
    val grid = _g.toMutableGrid()
    var nextOpenAirCandidate = grid.withIndex().first { (loc, item) -> grid.hasAnyOpenAirNeighbours(item, loc) }
    while (nextOpenAirCandidate != null) {
        grid[nextOpenAirCandidate.location] = OPEN_AIR
        nextOpenAirCandidate = grid.withIndex().first { (loc, item) -> grid.hasAnyOpenAirNeighbours(item, loc) }
    }

    println(grid)
    return grid.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, grid)
    }
}

private fun Grid<Char>.hasAnyOpenAirNeighbours(item: Char, loc: Coordinate) =
    item == UNVISITED
        && loc.getSurroundingManhattan()
    .any { !this.contains(it) || this[it] == OPEN_AIR }

private fun countExposedSides(coord: Coordinate, grid: Grid<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}

fun parse2d(input: String): Grid<Char> {
    return boolGridFromCoordinates(input).map { if(it) DROPLET else UNVISITED }
}