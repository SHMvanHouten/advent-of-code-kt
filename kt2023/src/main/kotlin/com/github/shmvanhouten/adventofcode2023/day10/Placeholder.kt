package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
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

fun countEnclosedTiles(_grid: Grid<Char>, startingDirection: Direction = NORTH): Int {
    val grid = _grid.map { Tile(it) }.toMutableGrid()
    val startingPoint = grid.firstCoordinateMatching { it.tile == 'S' }!!
    val loop: List<Pair<Coordinate, Direction>> = generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
        val (tile, _) = grid[loc]
        grid[loc] = Tile(tile, Status.PIPE)

        markAdjacentTiles(loc, direction, grid, tile)


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
    }.takeWhile { grid[it.first].tile != 'S' }.toList()

    fillOutFromTiles(grid)


    println(grid.map {
        when(it.status) {
            Status.UNVISITED -> it.tile
            Status.PIPE -> 'P'
            Status.LEFT -> '█'
            Status.RIGHT -> 'X'
        }
    })
    return grid.count { it.status == Status.RIGHT }
}

private fun markAdjacentTiles(
    loc: Coordinate,
    direction: Direction,
    grid: MutableGrid<Tile>,
    tile: Char
) {
//    val locLeft = loc.toTheLeft(direction)
//    val leftTile = grid.getOrNull(locLeft)
//    if (leftTile?.status == Status.UNVISITED) grid[locLeft] = leftTile.copy(status = Status.LEFT)
    // todo: left
    val locs = listLocsToTheRight(loc, direction, tile)
    for(locRight in locs) {
        val rightTile = grid.getOrNull(locRight)
        if (rightTile?.status == Status.UNVISITED) grid[locRight] = rightTile.copy(status = Status.RIGHT)
    }
}

private fun listLocsToTheRight(
    loc: Coordinate,
    direction: Direction,
    tile: Char
): List<Coordinate> {
    return when(tile) {
        '|', '-' -> listOf(loc.move(direction.turnRight()))
        'L' -> {
            when(direction) {
                WEST -> emptyList()
                SOUTH -> listOf(loc.move(WEST), loc.move(SOUTH))
                else -> error("should not move $direction when on tile $tile")
            }
        }
        'J' -> {
            when(direction) {
                EAST -> listOf(loc.move(SOUTH), loc.move(EAST))
                SOUTH -> emptyList()
                else -> error("should not move $direction when on tile $tile")
            }
        }
        '7' -> {
            when(direction) {
                NORTH -> listOf(loc.move(EAST), loc.move(NORTH))
                EAST -> emptyList()
                else -> error("should not move $direction when on tile $tile")
            }
        }
        'F' -> when(direction) {
            NORTH -> emptyList()
            WEST -> listOf(loc.move(NORTH), loc.move(WEST))
            else -> error("should not move $direction when on tile $tile")
        }
        'S' -> emptyList() // only because we have an L to start
        else -> error("unknown tile $tile")
    }
}

fun fillOutFromTiles(grid: MutableGrid<Tile>) {
    // todo grid.groupBy{it.} partitionBy
    val leftTiles = grid.coordinatesMatching { it.status == Status.LEFT }
    val rightTiles = grid.coordinatesMatching { it.status == Status.RIGHT }
//    fillOutFromTile(grid, leftTiles, Status.LEFT)
    fillOutFromTile(grid, rightTiles, Status.RIGHT)
}

fun fillOutFromTile(grid: MutableGrid<Tile>, _locs: List<Coordinate>, status: Status) {
    val unchecked = _locs.flatMap { it.getSurroundingManhattan() }.distinct().filter { grid.getOrNull(it)?.status == Status.UNVISITED }.toMutableList()
    while (unchecked.isNotEmpty()) {
        val next = unchecked.removeLast()
        val nextTile = grid.getOrNull(next)
        if(nextTile != null && nextTile.status == Status.UNVISITED) {
            grid[next] = nextTile.copy(status = status)
            unchecked += next.getSurroundingManhattan()
        }
    }
}

data class Tile(val tile: Char, val status: Status = Status.UNVISITED)

enum class Status {
    UNVISITED,
    PIPE,
    LEFT,
    RIGHT
}
