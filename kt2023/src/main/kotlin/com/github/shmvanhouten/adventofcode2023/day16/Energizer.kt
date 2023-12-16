package com.github.shmvanhouten.adventofcode2023.day16

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.charGridFromPicture

fun Grid<Tile>.countEnergizedTiles() = this.count { it.energizedDirections.isNotEmpty() }

fun energizeFromTopLeftGoingRight(input: String): Grid<Tile> {
    val grid = charGridFromPicture(input).map { Tile(it) }.toMutableGrid()
    return beam(Coordinate(0, 0), EAST, grid)
}

fun findMostEnergized(input: String): Int {
    val grid = charGridFromPicture(input).map { Tile(it) }
    val energizedGrids = 0.until(grid.width).map { beam(Coordinate(it, 0), SOUTH, grid.toMutableGrid()) } +
    0.until(grid.width).map { beam(Coordinate(it, grid.height - 1), NORTH, grid.toMutableGrid()) } +
    0.until(grid.height).map { beam(Coordinate(0, it), EAST, grid.toMutableGrid()) } +
    0.until(grid.height).map { beam(Coordinate(grid.width - 1, it), WEST, grid.toMutableGrid()) }
    return energizedGrids.maxOf { it.countEnergizedTiles() }
}

fun beam(starting: Coordinate, startingDirection: Direction, grid: MutableGrid<Tile>): Grid<Tile> {
    val beamLocations = mutableListOf(starting to startingDirection)

    while (beamLocations.isNotEmpty()) {
        val (location, direction) = beamLocations.removeLast()
        if(!grid.contains(location)) continue
        val tile = grid[location]
        if(tile.energizedDirections.contains(direction)) continue

        grid[location] = tile.copy(energizedDirections = tile.energizedDirections + direction)

        when(tile.tileType) {
            '.' -> beamLocations += location.move(direction) to direction
            '/', '\\' -> beamLocations += mirror(tile.tileType, location, direction)
            '|', '-' -> beamLocations += goThroughSplitter(tile.tileType, location, direction)
            else -> error("unknown tileType ${tile.tileType}")
        }
    }
    return grid
}

fun goThroughSplitter(tileType: Char, location: Coordinate, direction: Direction): List<Pair<Coordinate, Direction>> {
    val newDirections = when(tileType) {
        '|' -> {
            when(direction) {
                NORTH, SOUTH -> listOf(direction)
                EAST, WEST -> listOf(direction.turnLeft(), direction.turnRight())
            }
        }
        '-' -> {
            when(direction) {
                NORTH, SOUTH -> listOf(direction.turnLeft(), direction.turnRight())
                EAST, WEST -> listOf(direction)
            }
        }
        else -> error("unknown splitter $tileType")
    }
    return newDirections.map { location.move(it) to it }
}

fun mirror(tileType: Char, location: Coordinate, direction: Direction): Pair<Coordinate, Direction> {
    val newDirection = when(tileType) {
        '/' -> {
            when(direction) {
                NORTH, SOUTH -> direction.turnRight()
                EAST, WEST -> direction.turnLeft()
            }
        }
        '\\' -> {
            when(direction) {
                NORTH, SOUTH -> direction.turnLeft()
                EAST, WEST -> direction.turnRight()
            }
        }
        else -> error("unknown mirror $tileType")
    }
    return location.move(newDirection) to newDirection
}

data class Tile(val tileType: Char, val energizedDirections: List<Direction> = emptyList()) {
    fun toChar(): Char {
        return if(tileType != '.' || energizedDirections.isEmpty()) tileType
        else if(energizedDirections.size > 1) energizedDirections.size.digitToChar()
        else {
            when(energizedDirections.first()) {
                NORTH -> '^'
                EAST -> '>'
                SOUTH -> 'v'
                WEST -> '<'
            }
        }
    }
}
