package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.github.shmvanhouten.adventofcode.utility.lambda.identity

fun toCoordinate(input: String): Coordinate {
    val (x, y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x, y)
}

fun String.toCoordinateMap(): Map<Coordinate, Char> = this.toCoordinateMap { c, _ -> identity(c) }

fun String.toIntByCoordinateMap() = this.toCoordinateMap{ c, _ -> c.digitToInt() }

fun <T>String.toCoordinateMap(mappingFunction: (Char, Coordinate) -> T): Map<Coordinate, T> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c -> Coordinate(x,y) to mappingFunction(c, Coordinate(x,y)) }
    }.flatten().toMap()
}

/**
 * returns a set of coordinates for wherever the char is found in the string
 */
fun String.toCoordinateMap(targetChar: Char = '#'): Set<Coordinate> {
    return this.lines().mapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if(c == targetChar) {
                Coordinate(x, y)
            } else {
                null
            }
        }
    }.flatten().toSet()
}