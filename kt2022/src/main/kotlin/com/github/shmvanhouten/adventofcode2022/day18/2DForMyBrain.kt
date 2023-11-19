package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromCoordinates

const val DROPLET = '#'
const val UNVISITED = '.'
const val UNKNOWN = '?'
const val OPEN_AIR = ' '

fun countExposedSides(grid: Grid<Char>): Long {
    val map = fillInAirPockets(grid)

    println(map)
    return map.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, map)
    }
}

private fun fillInAirPockets(grid: Grid<Char>): Grid<Char> {
    val mutableGrid = grid.toMutableGrid()
    mutableGrid.forEachIndexed { (x, y), element ->
        if(element == UNVISITED) {
            val surrounding = Coordinate(x, y).getSurroundingManhattan()
            if(surrounding.any { !grid.contains(it) } || surrounding.any { mutableGrid[it] == OPEN_AIR }) {
                mutableGrid.replaceAllUnknown2dWithOpenAir(x, y)

            } else if(surrounding.all { mutableGrid[it] == DROPLET }){
                mutableGrid[Coordinate(x, y)] = DROPLET

            } else {
                mutableGrid[Coordinate(x, y)] = UNKNOWN
            }
        }
    }

    return mutableGrid
}

internal fun  MutableGrid<Char>.replaceAllUnknown2dWithOpenAir(
    x: Int,
    y: Int,
    finalCall: (Int, Int) -> Unit = { x1, y1 -> this.replaceAllUnknown2dWithOpenAir( x1, y1)
}) {
    this.replaceAllUnknownOnY(x, y, { i -> i - 1 }, finalCall)
    this.replaceAllUnknownOnY(x, y + 1, { i -> i + 1 }, finalCall)
}

internal tailrec fun  MutableGrid<Char>.replaceAllUnknownOnY(
    x: Int,
    y: Int,
    function: (Int) -> Int,
    finalCall: (Int, Int) -> Unit
) {
    if(!0.until(height).contains(y) || this[x, y] == OPEN_AIR || this[x,y] == DROPLET) return
    this[x, y] = OPEN_AIR
    replaceAllUnknownToLeft(x, y, finalCall)
    replaceAllUnknownToRight(x, y, finalCall)
    replaceAllUnknownOnY(x, function(y), function, finalCall)
}

internal fun MutableGrid<Char>.replaceAllUnknownToLeft(
    x: Int,
    y: Int,
    finalCall: (Int, Int) -> Unit
) {
    (x - 1).downTo(0).takeWhile { x1 -> this[x1, y] == UNKNOWN || this[x1, y] == UNVISITED }
        .forEach { x1 -> finalCall(x1, y)}
}

internal fun MutableGrid<Char>.replaceAllUnknownToRight(
    x: Int,
    y: Int,
    finalCall: (Int, Int) -> Unit
) {
    (x + 1).until(width).takeWhile { x1 -> this[x1, y] == UNKNOWN || this[x1, y] == UNVISITED }
        .forEach { x1 -> finalCall(x1, y)}
}

private fun countExposedSides(coord: Coordinate, grid: Grid<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}

fun parse2d(input: String): Grid<Char> {
    return boolGridFromCoordinates(input).map { if(it) DROPLET else UNVISITED }
}