package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    val grid: Grid<Char> = readFile("/input-day10.txt")
        .let { charGrid(it) }
        .also { println(it) }
}

fun farthestAwayFromStart(grid: Grid<Char>, startingDirection: Direction = NORTH): Int {
    val (_, loop) = workOutLoop(grid, startingDirection)
    return (loop.count() + 1)/2
}

fun countEnclosedTiles(_grid: Grid<Char>, startingDirection: Direction = NORTH): Int {
    val (startingPoint, loop) = workOutLoop(_grid, startingDirection)
    val grid = _grid.toMutableGrid()
    grid.matchingCoordinatesTo(loop.toList() + startingPoint, '#')
    grid.allMatchingTo('.') {it != '#'}
    fillOutUnenclosed(grid)

    val open_candidates = grid.coordinatesMatching { it == '.' }
        .filter { it.getSurrounding().none { l -> grid[l] == ' ' } }
    val tmpGrid = _grid.toMutableGrid()
    tmpGrid.matchingCoordinatesTo(open_candidates, 'I')
    println(tmpGrid)
    return open_candidates
        .count()
}

private fun workOutLoop(
    grid: Grid<Char>,
    startingDirection: Direction
): Pair<Coordinate, Sequence<Coordinate>> {
    val startingPoint = grid.firstCoordinateMatching { it == 'S' }!!
    val loop = generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
        val tile = grid[loc]
        val newDirection = when (tile) {
            '|' -> direction
            '-' -> direction
            'L' -> if (direction == SOUTH) EAST else NORTH
            'J' -> if (direction == SOUTH) WEST else NORTH
            '7' -> if (direction == NORTH) WEST else SOUTH
            'F' -> if (direction == NORTH) EAST else SOUTH
            'S' -> startingDirection
            else -> error("unknown tile $tile")
        }
        loc.move(newDirection) to newDirection
    }.takeWhile { grid[it.first] != 'S' }
    return startingPoint to loop.map { it.first }
}