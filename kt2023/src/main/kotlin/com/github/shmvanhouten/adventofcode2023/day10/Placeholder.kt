package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
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
            else -> error("unknown tile")
        }
        loc.move(newDirection) to newDirection
    }.takeWhile { grid[it.first] != 'S' }
    return (loop.count() + 1)/2
}