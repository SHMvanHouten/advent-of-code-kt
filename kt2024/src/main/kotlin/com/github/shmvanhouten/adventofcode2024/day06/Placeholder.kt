package com.github.shmvanhouten.adventofcode2024.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
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

    val positions = mutableSetOf<Coordinate>()
    var currentPosition = guardLocation
    var currentDirection = Direction.NORTH
    while (true) {
        positions += currentPosition
        val nextPos = currentPosition.move(currentDirection)
        if(!grid.contains(nextPos)) {
            return positions.count()
        } else if(grid[nextPos]) {
            currentDirection = currentDirection.turnRight()
        } else {
            positions += currentPosition
            currentPosition = nextPos
        }
    }
}
