package com.github.shmvanhouten.adventofcode2023.day16

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.gridTo

fun Grid<Tile>.countEnergizedTiles() = this.count { it.energizedDirections.isNotEmpty() }

fun energizeFromTopLeftGoingRight(input: String): Grid<Tile> {
    val grid = gridTo(input, ::Tile).toMutableGrid()
    return beam(Coordinate(0, 0), EAST, grid)
}

fun findMostEnergized(input: String): Int {
    val grid = gridTo(input, ::Tile)
    return collectStartingPositions(grid)
        .map { (loc, dir) -> beam(loc, dir, grid.toMutableGrid()) }
        .maxOf { it.countEnergizedTiles() }
}

private fun beam(starting: Coordinate, startingDirection: Direction, grid: MutableGrid<Tile>): Grid<Tile> {
    val beamLocations = mutableListOf(starting to startingDirection)

    while (beamLocations.isNotEmpty()) {
        val (location, direction) = beamLocations.removeLast()
        val tile = grid.getOrNull(location)
        if (tile != null && !tile.energizedDirections.contains(direction)) {
            grid[location] = tile.copy(energizedDirections = tile.energizedDirections + direction)
            beamLocations += getNewDirections(tile.tileType, direction)
                .map { location.move(it) to it }
        }
    }
    return grid
}

private fun getNewDirections(tileType: Char, direction: Direction): List<Direction> =
    when (tileType) {
        '.' -> listOf(direction)
        '/' -> {
            when (direction) {
                NORTH, SOUTH -> listOf(direction.turnRight())
                EAST, WEST -> listOf(direction.turnLeft())
            }
        }

        '\\' -> {
            when (direction) {
                NORTH, SOUTH -> listOf(direction.turnLeft())
                EAST, WEST -> listOf(direction.turnRight())
            }
        }

        '|' -> {
            when (direction) {
                NORTH, SOUTH -> listOf(direction)
                EAST, WEST -> listOf(direction.turnLeft(), direction.turnRight())
            }
        }

        '-' -> {
            when (direction) {
                NORTH, SOUTH -> listOf(direction.turnLeft(), direction.turnRight())
                EAST, WEST -> listOf(direction)
            }
        }

        else -> error("unknown tileType $tileType")
    }

private fun collectStartingPositions(grid: Grid<Tile>) =
    0.until(grid.width).map { Coordinate(it, 0) to SOUTH } +
            0.until(grid.width).map { Coordinate(it, grid.height - 1) to NORTH } +
            0.until(grid.height).map { Coordinate(0, it) to EAST } +
            0.until(grid.height).map { Coordinate(grid.width - 1, it) to WEST }

data class Tile(val tileType: Char, val energizedDirections: List<Direction> = emptyList()) {
    fun toChar(): Char =
        if (tileType != '.' || energizedDirections.isEmpty()) tileType
        else if (energizedDirections.size > 1) energizedDirections.size.digitToChar()
        else {
            when (energizedDirections.first()) {
                NORTH -> '^'
                EAST -> '>'
                SOUTH -> 'v'
                WEST -> '<'
            }
        }
}
