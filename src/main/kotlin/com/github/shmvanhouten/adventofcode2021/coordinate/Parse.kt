package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.lambda.identity

fun String.toCoordinateMap(): Map<Coordinate, Char> = this.toCoordinateMap { identity(it) }

fun String.toIntByCoordinateMap() = this.toCoordinateMap(Char::digitToInt)

fun <T>String.toCoordinateMap(mappingFunction: (Char) -> T): Map<Coordinate, T> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coordinate(x,y) to mappingFunction(c) }
    }.flatten().toMap()
}
