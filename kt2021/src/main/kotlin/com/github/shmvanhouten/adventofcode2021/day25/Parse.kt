package com.github.shmvanhouten.adventofcode2021.day25

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.collectors.extremes
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

fun toSeaCucumbers(input: String): FieldOfSeaCucumbers {
    val coordinatesByChar = input.toCoordinateMap().entries.map { it.value to it.key }
        .groupBy { it.first }
        .mapValues { it.value.map { p -> p.second }.toSet() }
    return FieldOfSeaCucumbers(coordinatesByChar['>']!!, coordinatesByChar['v']!!, getDimensions(coordinatesByChar.values.flatten()))
}

fun getDimensions(coordinates: List<Coordinate>): Square {
    val (minX, maxX) = coordinates.map { it.x }.extremes()!!
    val (minY, maxY) = coordinates.map { it.y }.extremes()!!
    return Square(minX..maxX, minY..maxY)
}
