package com.github.shmvanhouten.adventofcode2024.day06

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.MutableGrid
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromPicture
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun countVisitedLocations(input: String): Int {
    val guardLocation = charGrid(input).firstCoordinateMatching { it == '^' }!!
    val grid = boolGridFromPicture(input, '#')

    return listVisitedLocations(guardLocation, grid).count()
}

fun findLocationsToLoopGuard(input: String): List<Coordinate> {
    val guardLocation = charGrid(input).firstCoordinateMatching { it == '^' }!!
    val grid = boolGridFromPicture(input, '#')

    val mutableGrid = grid.toMutableGrid()

    return (listVisitedLocations(guardLocation, grid) - guardLocation)
        .filter {isALoopWithObstacleAt(mutableGrid, guardLocation, obstacle = it)}
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

private fun isALoopWithObstacleAt(grid: MutableGrid<Boolean>, guardLocation: Coordinate, obstacle: Coordinate): Boolean {
    grid[obstacle] = true
    val isALoop = isALoop(guardLocation, grid)
    grid[obstacle] = false
    return isALoop
}

private fun isALoop(
    guardLocation: Coordinate,
    grid: MutableGrid<Boolean>
): Boolean {
    val positionsWithDirections = mutableSetOf<Pair<Coordinate, Direction>>()
    var currentPosition = guardLocation
    var currentDirection = Direction.NORTH
    while (true) {
        positionsWithDirections += currentPosition to currentDirection
        val nextPos = currentPosition.move(currentDirection)
        if (!grid.contains(nextPos)) {
            return false
        } else if (positionsWithDirections.contains(nextPos to currentDirection)) {
            return true
        } else if (grid[nextPos]) {
            currentDirection = currentDirection.turnRight()
        } else {
            currentPosition = nextPos
        }
    }
}
