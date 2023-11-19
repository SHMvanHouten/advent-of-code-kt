package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.grid.*

const val DROPLET = '#'
const val UNVISITED = '.'
const val UNKNOWN = '?'
const val OPEN_AIR = ' '

fun countExposedSides(grid: IGrid<Char>): Long {
    val map = fillInAirPockets(grid as Grid<Char>)

    return map.sumOfIndexed{ coord, pixel ->
        if(pixel == OPEN_AIR) 0
        else countExposedSides(coord, map)
    }
}

private fun fillInAirPockets(grid: Grid<Char>): Grid<Char> {
    val mutableGrid = grid.toMutableGrid()
    mutableGrid.forEachIndexed { x, y, element ->
        if(element == UNVISITED) {
            val surrounding = Coord(x, y).getSurroundingManhattan()
            if(surrounding.any { !grid.contains(it) } || surrounding.any { mutableGrid[it] == OPEN_AIR }) {
                mutableGrid.replaceAllUnknownAroundWithOpenAir(x, y)
                mutableGrid[Coord(x, y)] = OPEN_AIR
            } else if(surrounding.all { mutableGrid[it] == DROPLET }){
                mutableGrid[Coord(x, y)] = DROPLET
            } else {
                mutableGrid[Coord(x, y)] = UNKNOWN
            }
        }
    }

    return mutableGrid
}

private fun  MutableGrid<Char>.replaceAllUnknownAroundWithOpenAir(x: Int, y: Int) {
    this.replaceAllUnknownToRight(x, y, OPEN_AIR)
    this.replaceAllUnknownToLeft(x, y, OPEN_AIR)
    this.replaceAllUnknownAbove(x, y - 1)
    this.replaceAllUnknownBelow(x, y + 1)
}

private tailrec fun  MutableGrid<Char>.replaceAllUnknownBelow(x: Int, y: Int, c: Char = OPEN_AIR) {
    if(y >= height || this[x, y] != UNKNOWN) return
    this[x, y] = c
    replaceAllUnknownToLeft(x, y, c)
    replaceAllUnknownToRight(x, y, c)
    replaceAllUnknownBelow(x, y + 1)
}

private tailrec fun  MutableGrid<Char>.replaceAllUnknownAbove(x: Int, y: Int, c: Char = OPEN_AIR) {
    if(y < 0 || this[x, y] != UNKNOWN) return
    this[x, y] = c
    replaceAllUnknownToLeft(x, y, c)
    replaceAllUnknownToRight(x, y, c)
    replaceAllUnknownAbove(x, y - 1)
}

private fun MutableGrid<Char>.replaceAllUnknownToLeft(
    x: Int,
    y: Int,
    c: Char
) {
    (x - 1).downTo(0).takeWhile { x1 -> this[x1, y] == UNKNOWN }
        .forEach { x1 -> this[x1, y] = c }
}

private fun MutableGrid<Char>.replaceAllUnknownToRight(
    x: Int,
    y: Int,
    c: Char
) {
    (x + 1).until(width).takeWhile { x1 -> this[x1, y] == UNKNOWN }
        .forEach { x1 -> this[x1, y] = c }
}

private fun countExposedSides(coord: Coord, grid: Grid<Char>): Long {
    return coord.getSurroundingManhattan()
        .count { !grid.contains(it) || grid[it] == OPEN_AIR }
        .toLong()
}

fun parse2d(input: String): IGrid<Char> {
    return boolGridFromCoordinates(input).map { if(it) DROPLET else UNVISITED }
}