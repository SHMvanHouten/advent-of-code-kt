package com.github.shmvanhouten.adventofcode2022.day14

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun parse(input: String): Set<Coordinate> {
    return input.lines().flatMap { listRockCoordinates(it) }.toSet()
}

fun listRockCoordinates(input: String): List<Coordinate> {
    return input.split(" -> ")
        .map{it.toCoordinate()}
        .windowed(2)
        .flatMap { (one, other) -> one..other }
}

private fun String.toCoordinate(): Coordinate {
    val (x, y) = splitIntoTwo(",")
    return Coordinate(x.toInt(), y.toInt())
}
