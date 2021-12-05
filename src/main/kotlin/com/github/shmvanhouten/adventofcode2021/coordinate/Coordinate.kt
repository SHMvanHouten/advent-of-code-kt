package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun toCoordinate(input: String): Coordinate {
    val (x,y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x,y)
}

operator fun Coordinate.rangeTo(otherCoordinate: Coordinate): List<Coordinate> {
    return if (x == otherCoordinate.x) {
        (y..otherCoordinate.y)
            .map { y -> Coordinate(x, y) }
    } else if (y == otherCoordinate.y) {
        (x..otherCoordinate.x)
            .map { x -> Coordinate(x, y) }
    } else {
        // todo: support diagonal
        error("does not support diagonal")
    }
}

