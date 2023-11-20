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
    val grid = _g.toMutable3dGrid().surroundWith(UNVISITED) // make sure we don't get air pockets on the side
    grid.aerateAllFrom(Coordinate3d(0, 0, 0))

    return grid.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, grid)
    }
}

private fun Mutable3dGrid<Char>.aerateAllFrom(aerablePoint: Coordinate3d) {
    val aerablePoints = mutableListOf(aerablePoint)
    while (aerablePoints.isNotEmpty()) {
        val point = aerablePoints.removeLast()
        this[point] = OPEN_AIR
        aerablePoints.addAll(this.surroundingUnVisited(point))
    }
}

private fun Mutable3dGrid<Char>.surroundingUnVisited(nextOpenAirCandidate: Coordinate3d): List<Coordinate3d> {
    return nextOpenAirCandidate.getSurroundingManhattan()
        .filter { contains(it) && this[it] == UNVISITED }
}

private fun countExposedSides(coord: Coordinate3d, grid: Grid3d<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}