package com.github.shmvanhouten.adventofcode2021.day05

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun toNonDiagonalVentMap(input: String): Map<Coordinate, Occurrence> {
    return input.lines()
        .asSequence()
        .map { toCoordinateProgression(it) }
        .filter { it.isHorizontal() || it.isVertical() }
        .flatten()
        .groupingBy { it }.eachCount()
}

fun toVentMap(input: String): Map<Coordinate, Occurrence> {
    return input.lines()
        .map { toCoordinateProgression(it) }
        .flatten()
        .groupingBy { it }.eachCount()
}

fun toCoordinateProgression(line: String): CoordinateProgression {
    return line.split(" -> ")
        .map { toCoordinate(it) }
        .let { (c1, c2) -> c1..c2 }
}

typealias Occurrence = Int
