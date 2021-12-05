package com.github.shmvanhouten.adventofcode2021.coordinate

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun toCoordinate(input: String): Coordinate {
    val (x, y) = input.split(",")
        .map { it.toInt() }
    return Coordinate(x, y)
}

operator fun Coordinate.rangeTo(otherCoordinate: Coordinate): List<Coordinate> {
    return when {
        x == otherCoordinate.x -> generateVerticalRange(otherCoordinate)
        y == otherCoordinate.y -> generateHorizontalRange(otherCoordinate)
        else -> generateDiagonalRange(otherCoordinate)
    }
}

fun upOrDownProgression(n1: Int, n2: Int): IntProgression =
    if (n1 <= n2) (n1..n2)
    else n1.downTo(n2)

private fun Coordinate.generateVerticalRange(otherCoordinate: Coordinate) =
    upOrDownProgression(y, otherCoordinate.y)
        .map { y -> Coordinate(x, y) }

private fun Coordinate.generateHorizontalRange(otherCoordinate: Coordinate) =
    upOrDownProgression(x, otherCoordinate.x)
        .map { x -> Coordinate(x, y) }

private fun Coordinate.generateDiagonalRange(otherCoordinate: Coordinate): List<Coordinate> {
    val xRange = upOrDownProgression(x, otherCoordinate.x).toList()
    val yRange = upOrDownProgression(y, otherCoordinate.y).toList()
    if (xRange.size != yRange.size) error("Diagonal line is not 45 degrees! $xRange, $yRange")
    return xRange
        .zip(yRange)
        .map { (x, y) -> Coordinate(x, y) }
}

