package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun toCoordinate(input: String): Coordinate {
    val (x, y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x, y)
}

operator fun Coordinate.rangeTo(otherCoordinate: Coordinate): CoordinateProgression {
    return CoordinateProgression(this, otherCoordinate)
}
