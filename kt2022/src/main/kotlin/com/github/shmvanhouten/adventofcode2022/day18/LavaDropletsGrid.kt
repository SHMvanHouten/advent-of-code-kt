package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.grid.Grid3d
import com.github.shmvanhouten.adventofcode.utility.grid.Mutable3dGrid

// PART 1
fun countExposedSidesNoBubble(grid: Grid3d<Char>): Int {
    return grid.withIndex().filter { it.item == DROPLET }
        .sumOf { (loc, _) ->
            6 - loc.getSurroundingManhattan().count { grid.contains(it) && grid[it] == DROPLET }
        }
}

// PART 2
fun countExposedSides(_g: Grid3d<Char>): Long {
    val grid = _g.toMutable3dGrid()
    var nextOpenAirCandidate = grid.firstCoordinateMatchingIndexed { loc, item -> grid.hasAnyOpenAirNeighbours(item, loc) }
    while (nextOpenAirCandidate != null) { // not necessary for our input, but it could happen...
        grid.aerateSurrounding(nextOpenAirCandidate)
        nextOpenAirCandidate = grid.firstCoordinateMatchingIndexed { loc, item -> grid.hasAnyOpenAirNeighbours(item, loc) }
    }

    return grid.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, grid)
    }
}

private fun Mutable3dGrid<Char>.aerateSurrounding(nextOpenAirCandidate: Coordinate3d) {
    this[nextOpenAirCandidate] = OPEN_AIR
    nextOpenAirCandidate.getSurroundingManhattan()
        .filter { this.contains(it) && this[it] == UNVISITED }
        .forEach { aerateSurrounding(it) }
}

private fun Grid3d<Char>.hasAnyOpenAirNeighbours(item: Char, loc: Coordinate3d) =
    item == UNVISITED
            && loc.getSurroundingManhattan()
        .any { !this.contains(it) || this[it] == OPEN_AIR }

private fun countExposedSides(coord: Coordinate3d, grid: Grid3d<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}