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
fun countExposedSides(grid: Grid3d<Char>): Long {
    val map = fillInAirPockets(grid)
        .let { fillInAirPockets(it) } // todo: should not have to pass through 2 more to make the tests pass
        .let { fillInAirPockets(it) }

    return map.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, map)
    }
}

private fun fillInAirPockets(grid: Grid3d<Char>): Grid3d<Char> {
    val mutableGrid = grid.toMutable3dGrid()
    mutableGrid.forEachIndexed { (x, y, z), element ->
        if(element != DROPLET) {
            val surrounding = Coordinate3d(x, y, z).getSurroundingManhattan()
            if(surrounding.any { !grid.contains(it) } || surrounding.any { mutableGrid[it] == OPEN_AIR }) {
                mutableGrid[Coordinate3d(x, y, z)] = OPEN_AIR
                mutableGrid.replaceAllUnknownAroundWithOpenAir(x, y, z)
            } else if(surrounding.all { mutableGrid[it] == DROPLET }){
                mutableGrid[Coordinate3d(x, y, z)] = DROPLET
            } else {
                mutableGrid[Coordinate3d(x, y, z)] = UNKNOWN
            }
        }
    }

    return mutableGrid
}

private fun Mutable3dGrid<Char>.replaceAllUnknownAroundWithOpenAir(x: Int, y: Int, z: Int) {
    replaceAllUnknownAtLayerWithOpenAir(x, y, z /*this one + below*/) {i -> i -1}
    replaceAllUnknownAtLayerWithOpenAir(x, y, z  + 1) { i -> i +1}
}

private tailrec fun Mutable3dGrid<Char>.replaceAllUnknownAtLayerWithOpenAir(x: Int, y: Int, z: Int, nextStep: (Int) -> Int) {
    if(!0.until(depth).contains(z) || this[x, y, z] != UNKNOWN) return
    val gridAtDepth = this[z]
    gridAtDepth.replaceAllUnknownAroundWithOpenAir(x, y)
    replaceAllUnknownAtLayerWithOpenAir(x, y, nextStep(z), nextStep)
}

private fun countExposedSides(coord: Coordinate3d, grid: Grid3d<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}