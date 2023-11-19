package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun parse(input: String): Set<Coordinate> {
    return input.lines().flatMap { listRockCoordinates(it) }.toSet()
}

fun parseToGrid(input: String): CaveGrid {
    val rockLocations = parse(input)
    val minX = 0
    val maxX = rockLocations.maxOf { it.x } + 200
    val minY = 0
    val floor = rockLocations.maxOf { it.y } + 2

    val grid = (minY..floor).map { y ->
        (minX..maxX).map { x ->
            if (rockLocations.contains(Coordinate(x, y))) ROCK
            else AIR
        }
    }
    return CaveGrid(Grid(grid).toMutableGrid(), floor)
}

private fun listRockCoordinates(input: String): List<Coordinate> {
    return input.split(" -> ")
        .map{it.toCoordinate()}
        .windowed(2)
        .flatMap { (one, other) -> one..other }
}

private fun String.toCoordinate(): Coordinate {
    val (x, y) = splitIntoTwo(",")
    return Coordinate(x.toInt(), y.toInt())
}
