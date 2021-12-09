package com.github.shmvanhouten.adventofcode2021.day09

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.RelativePosition
import java.util.*

fun locateBasins(heights: Map<Coordinate, Int>): Set<Set<Coordinate>> {
    val lowPoints = findLowPoints(heights)
    return lowPoints.map { mapOutBasin(it.key, heights) }.toSet()
}

fun mapOutBasin(lowPointLocation: Coordinate, heights: Map<Coordinate, Int>): Set<Coordinate> {
    val basin = mutableSetOf(lowPointLocation)
    val possibleLocations = LinkedList(getNeighbours(lowPointLocation))
    val visitedLocations = (possibleLocations + lowPointLocation).toMutableSet()
    while (possibleLocations.isNotEmpty()) {
        val possibleLocation = possibleLocations.poll()
        if (heights.containsKey(possibleLocation) && heights[possibleLocation]!! < 9) {
            basin += possibleLocation
            val newLocations = getNeighbours(possibleLocation).filter { !visitedLocations.contains(it) }
            possibleLocations.addAll(newLocations)
            visitedLocations.addAll(newLocations)
        }
    }
    return basin
}

fun sumRiskLevelsOfLowPoints(heights: Map<Coordinate, Int>) =
    findLowPoints(heights)
        .values.sumOf { it + 1 }

private fun findLowPoints(heights: Map<Coordinate, Int>) =
    heights.filter { (coordinate, value) -> getNeighbours(coordinate).all { neighbour -> value < heights[neighbour] ?: Int.MAX_VALUE } }

private fun getNeighbours(coordinate: Coordinate): Set<Coordinate> {
    return setOf(
        coordinate + RelativePosition.TOP.coordinate,
        coordinate + RelativePosition.RIGHT.coordinate,
        coordinate + RelativePosition.BOTTOM.coordinate,
        coordinate + RelativePosition.LEFT.coordinate,
    )
}