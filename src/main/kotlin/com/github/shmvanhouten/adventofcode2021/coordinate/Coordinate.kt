package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun toCoordinate(input: String): Coordinate {
    val (x, y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x, y)
}

operator fun Coordinate.rangeTo(otherCoordinate: Coordinate): List<Coordinate> {
    return when {
        x == otherCoordinate.x -> {
            (y..otherCoordinate.y)
                .map { y -> Coordinate(x, y) }
        }
        y == otherCoordinate.y -> {
            (x..otherCoordinate.x)
                .map { x -> Coordinate(x, y) }
        }
        else -> {
            val xRange = upOrDownProgression(x, otherCoordinate.x)
            val yRange = upOrDownProgression(y, otherCoordinate.y)
            if(xRange.toList().size != yRange.toList().size) error("Diagonal line is not 45 degrees! $xRange, $yRange")
            xRange
                .zip(yRange)
                .map { (x, y) -> Coordinate(x, y) }
        }
    }
}

fun upOrDownProgression(n1: Int, n2: Int): IntProgression {
    if (n1 <= n2) {
        return (n1..n2)
    } else {
        return n1.downTo(n2)
    }
}

