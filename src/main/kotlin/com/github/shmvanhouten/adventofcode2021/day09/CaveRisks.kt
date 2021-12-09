package com.github.shmvanhouten.adventofcode2021.day09

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.RelativePosition

fun sumRiskLevelsOfLowPoints(toCoordinateMap: Map<Coordinate, Int>) =
    toCoordinateMap.filter { (coordinate, value) -> getNeighbours(coordinate).all { neighbour -> value < toCoordinateMap[neighbour] ?: Int.MAX_VALUE } }
        .values.sumOf { it + 1 }

private fun getNeighbours(coordinate: Coordinate): Set<Coordinate> {
    return setOf(
        coordinate + RelativePosition.TOP.coordinate,
        coordinate + RelativePosition.RIGHT.coordinate,
        coordinate + RelativePosition.BOTTOM.coordinate,
        coordinate + RelativePosition.LEFT.coordinate,
    )
}