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
    val map = explore(grid)
        .also { g ->
            g.withIndex().firstCoordinateMatching{ (l, c) ->
            c == UNKNOWN && l.getSurroundingManhattan().any { !g.contains(it) || g[it] == OPEN_AIR }
        }.also { println(it) } }
        .let { explore(it) } // todo: should not have to pass through again to make the tests pass

    return map.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, map)
    }
}

private fun explore(grid: Grid3d<Char>): Grid3d<Char> {
    val mutableGrid = grid.toMutable3dGrid()
    mutableGrid.forEachIndexed { (x, y, z), element ->
        if(element != DROPLET) {
            val surrounding = Coordinate3d(x, y, z).getSurroundingManhattan()
            if(surrounding.any { !grid.contains(it) } || surrounding.any { mutableGrid[it] == OPEN_AIR }) {
                mutableGrid.replaceAllUnknown3dWithOpenAir(x, y, z)
            } else if(surrounding.all { mutableGrid[it] == DROPLET }){
                mutableGrid[Coordinate3d(x, y, z)] = DROPLET
            } else {
                mutableGrid[Coordinate3d(x, y, z)] = UNKNOWN
            }
        }
    }

    return mutableGrid
}

private fun Mutable3dGrid<Char>.replaceAllUnknown3dWithOpenAir(x: Int, y: Int, z: Int) {
    this[Coordinate3d(x, y, z)] = OPEN_AIR
    replaceAllUnknownAtLayerWithOpenAir(x, y, z /*this one + below*/) {i -> i -1}
    replaceAllUnknownAtLayerWithOpenAir(x, y, z  + 1) { i -> i +1}
}

private tailrec fun Mutable3dGrid<Char>.replaceAllUnknownAtLayerWithOpenAir(x: Int, y: Int, z: Int, nextStep: (Int) -> Int) {
    if(!0.until(depth).contains(z)) return
    if(this[x, y, z] != UNKNOWN && this[x, y, z] != UNVISITED) return

    val gridAtDepth = this[z]
    gridAtDepth.replaceAllUnknown2dWithOpenAir(x, y) { x1, y1 -> this.replaceAllUnknown3dWithOpenAir(x1, y1, z) }
    replaceAllUnknownAtLayerWithOpenAir(x, y, nextStep(z), nextStep)
}

private fun countExposedSides(coord: Coordinate3d, grid: Grid3d<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}