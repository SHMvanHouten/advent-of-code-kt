package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.github.shmvanhouten.adventofcode.utility.lambda.identity

fun toCoordinate(input: String): Coordinate {
    val (x, y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x, y)
}

fun String.toCoordinateMap(): Map<Coordinate, Char> = this.toCoordinateMap { identity(it) }

fun String.toIntByCoordinateMap() = this.toCoordinateMap(Char::digitToInt)

fun <T>String.toCoordinateMap(mappingFunction: (Char) -> T): Map<Coordinate, T> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coordinate(x,y) to mappingFunction(c) }
    }.flatten().toMap()
}
