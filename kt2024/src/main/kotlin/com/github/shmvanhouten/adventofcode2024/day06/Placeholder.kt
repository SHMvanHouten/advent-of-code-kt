package com.github.shmvanhouten.adventofcode2024.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromPicture
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    readFile("/input-day06.txt")
        .lines()
        .onEach(::println)
}

fun countVisitedLocations(input: String): Int {
    val guardLocation = charGrid(input).firstCoordinateMatching { it == '^' }!!
    val grid = boolGridFromPicture(input, '#')

    return listVisitedLocations(guardLocation, grid).count()
}

// only put the obstacle in one of the 5329 visited locations

fun findLocationsToLoopGuard(input: String): List<Coordinate> {
    val guardLocation = charGrid(input).firstCoordinateMatching { it == '^' }!!
    val grid = boolGridFromPicture(input, '#')

    return (listVisitedLocations(guardLocation, grid) - guardLocation)
        .map {
            val toMutableGrid = grid.toMutableGrid()
            toMutableGrid[it] = true
            toMutableGrid to it
        }
        .filter { isALoop(it.first, guardLocation) }
        .map { it.second }
}

private fun listVisitedLocations(
    guardLocation: Coordinate,
    grid: Grid<Boolean>
): MutableSet<Coordinate> {
    val positions = mutableSetOf<Coordinate>()
    var currentPosition = guardLocation
    var currentDirection = Direction.NORTH
    while (true) {
        positions += currentPosition
        val nextPos = currentPosition.move(currentDirection)
        if (!grid.contains(nextPos)) {
            return positions
        } else if (grid[nextPos]) {
            currentDirection = currentDirection.turnRight()
        } else {
            currentPosition = nextPos
        }
    }
}

private fun isALoop(grid: MutableGrid<Boolean>, guardLocation: Coordinate): Boolean {
    val positionsWithDirections = mutableSetOf<Pair<Coordinate, Direction>>()
    var currentPosition = guardLocation
    var currentDirection = Direction.NORTH
    while (true) {
        positionsWithDirections += currentPosition to currentDirection
        val nextPos = currentPosition.move(currentDirection)
        if(!grid.contains(nextPos)) {
            return false
        } else if (positionsWithDirections.contains(nextPos to currentDirection)) {
            return true
        }else if(grid[nextPos]) {
            currentDirection = currentDirection.turnRight()
        } else {
            currentPosition = nextPos
        }
    }
}
