package com.github.shmvanhouten.adventofcode2023.day10

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid

fun farthestAwayFromStart(grid: Grid<Char>): Int {
    val (startingPoint, startingDirection: Direction) = getStartingPointAndDirection(grid)
    return (generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
        val tile = grid[loc]
        val newDirection = reOrient(tile, direction, startingDirection)
        loc.move(newDirection) to newDirection
    }.takeWhile { grid[it.first] != 'S' }.count() + 1) / 2
}

fun countEnclosedTiles(_grid: Grid<Char>): Int {
    val (startingPoint, startingDirection: Direction) = getStartingPointAndDirection(_grid)

    val grid = _grid.map { Tile(it) }.toMutableGrid()
    grid[startingPoint] = grid[startingPoint].copy(status = Status.PIPE)
    generateSequence(startingPoint.move(startingDirection) to startingDirection) { (loc, direction) ->
        val (tile, _) = grid[loc]
        grid[loc] = Tile(tile, Status.PIPE)

        markAdjacentTiles(loc, direction, grid, tile)

        val newDirection = reOrient(tile, direction, startingDirection)
        loc.move(newDirection) to newDirection
    }.takeWhile { grid[it.first].tile != 'S' }.toList()

    fillOutFromTiles(grid)

    printGrid(grid)
    val outsideStatus = findWhichSideOfLoopIsInside(grid)
    return grid.count { it.status == outsideStatus }
}

private fun reOrient(
    tile: Char,
    direction: Direction,
    startingDirection: Direction
) = when (tile) {
    '|' -> direction
    '-' -> direction
    'L' -> if (direction == SOUTH) EAST else NORTH
    'J' -> if (direction == SOUTH) WEST else NORTH
    '7' -> if (direction == NORTH) WEST else SOUTH
    'F' -> if (direction == NORTH) EAST else SOUTH
    'S' -> startingDirection
    else -> error("unknown tile $tile")
}

private fun getStartingPointAndDirection(grid: Grid<Char>): Pair<Coordinate, Direction> {
    val startingPoint = grid.firstCoordinateMatching { it == 'S' }!!
    val startingDirection: Direction = pickFirstDirection(startingPoint, grid)
    return Pair(startingPoint, startingDirection)
}

private fun pickFirstDirection(startingPoint: Coordinate, grid: Grid<Char>): Direction {
    return startingPoint.getSurroundingManhattanWithDirection().first { (c, direction) ->
        val neighbour = grid.getOrNull(c)
        neighbour != null && neighbour.isAccessibleFrom(direction.opposite())
    }.second
}

private fun Char.isAccessibleFrom(dir: Direction): Boolean = when (this) {
    '|' -> dir == NORTH || dir == SOUTH
    '-' -> dir == WEST || dir == EAST
    'L' -> dir == NORTH || dir == EAST
    'J' -> dir == NORTH || dir == WEST
    '7' -> dir == SOUTH || dir == WEST
    'F' -> dir == SOUTH || dir == EAST
    '.' -> false
    else -> error("unknown tile $this")
}

fun findWhichSideOfLoopIsInside(grid: MutableGrid<Tile>): Status {
    val outsideStatus = grid.perimiter().first { it.status == Status.LEFT || it.status == Status.RIGHT }.status
    return when(outsideStatus) {
        Status.LEFT -> Status.RIGHT
        Status.RIGHT -> Status.LEFT
        else -> error("Cannot happen")
    }
}

private fun markAdjacentTiles(
    loc: Coordinate,
    direction: Direction,
    grid: MutableGrid<Tile>,
    tile: Char
) {
    val locsLeft = listLocsToTheLeft(loc, direction, tile)
    for(locLeft in locsLeft) {
        val leftTile = grid.getOrNull(locLeft)
        if (leftTile?.status == Status.UNVISITED) grid[locLeft] = leftTile.copy(status = Status.LEFT)
    }
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
        'S' -> error("hey")
        else -> error("unknown tile $tile")
    }
}

private fun listLocsToTheLeft(
    loc: Coordinate,
    direction: Direction,
    tile: Char
): List<Coordinate> {
    return when(tile) {
        '|', '-' -> listOf(loc.move(direction.turnLeft()))
        'L' -> when(direction) {
            SOUTH -> emptyList()
            WEST -> listOf(loc.move(WEST), loc.move(SOUTH))
            else -> error("should not move $direction when on tile $tile")
        }
        'J' -> when(direction) {
            SOUTH -> listOf(loc.move(SOUTH), loc.move(EAST))
            EAST -> emptyList()
            else -> error("should not move $direction when on tile $tile")
        }
        '7' -> when(direction) {
            EAST -> listOf(loc.move(EAST), loc.move(NORTH))
            NORTH -> emptyList()
            else -> error("should not move $direction when on tile $tile")
        }
        'F' -> when(direction) {
            WEST -> emptyList()
            NORTH -> listOf(loc.move(NORTH), loc.move(WEST))
            else -> error("should not move $direction when on tile $tile")
        }
        else -> error("unknown tile $tile")
    }
}

fun fillOutFromTiles(grid: MutableGrid<Tile>) {
    val leftTiles = grid.coordinatesMatching { it.status == Status.LEFT }
    fillOutFromTile(grid, leftTiles, Status.LEFT)

    val rightTiles = grid.coordinatesMatching { it.status == Status.RIGHT }
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

private fun printGrid(grid: MutableGrid<Tile>) {
    val resetToBlack = "\u001b[30m"
    val yellow = "\u001b[33m"
    val blue = "\u001b[34m"
    val red = "\u001b[31m"
    println(grid.map {
        when (it.status) {
            Status.UNVISITED -> it.tile.toString()
            Status.PIPE -> "$blue${it.tile}$resetToBlack"
            Status.LEFT -> "$yellow█$resetToBlack"
            Status.RIGHT -> "$red█$resetToBlack"
        }
    })
}

private fun replace(c: Char) = when(c) {
    '7' -> '˥'
    'J' -> '˩'
    'F' -> 'Γ'
    else -> c
}

data class Tile(val tile: Char, val status: Status = Status.UNVISITED)

enum class Status {
    UNVISITED,
    PIPE,
    LEFT,
    RIGHT
}
