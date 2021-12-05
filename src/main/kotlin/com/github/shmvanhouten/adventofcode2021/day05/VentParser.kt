package com.github.shmvanhouten.adventofcode2021.day05

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.coordinate.rangeTo
import com.github.shmvanhouten.adventofcode2021.coordinate.toCoordinate

fun toNonDiagonalVentMap(input: String): Map<Coordinate, Occurrence> {
    return input.lines()
        .asSequence()
        .map { toCoordinateRange(it) }
        .filter { (c1, c2) -> isHorizontal(c1, c2) || isVertical(c1, c2) }
        .map{ (c1, c2) -> c1..c2 }
        .flatten()
        .groupingBy { it }.eachCount()
}

fun toVentMap(input: String): Map<Coordinate, Occurrence> {
    return input.lines()
        .map { toCoordinateRange(it) }
        .map{ (c1, c2) -> c1..c2 }
        .flatten()
        .groupingBy { it }.eachCount()
}

fun isVertical(c1: Coordinate, c2: Coordinate): Boolean {
    return c1.x == c2.x
}

fun isHorizontal(c1: Coordinate, c2: Coordinate): Boolean {
    return c1.y == c2.y
}

fun toCoordinateRange(line: String): List<Coordinate> {
    return line.split(" -> ")
        .map { toCoordinate(it) }
}

typealias Occurrence = Int
