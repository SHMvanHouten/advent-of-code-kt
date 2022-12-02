package com.github.shmvanhouten.adventofcode2021.day09

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import java.util.*

fun locateBasins(heights: Map<Coordinate, Height>): Set<Set<Coordinate>> {
    val lowPoints = findLowPoints(heights)
    return lowPoints.map { mapOutBasin(it.key, heights) }.toSet()
}

fun sumRiskLevelsOfLowPoints(heights: Map<Coordinate, Height>) =
    findLowPoints(heights)
        .values.sumOf { it + 1 }

private fun mapOutBasin(lowPointLocation: Coordinate, heights: Map<Coordinate, Height>): Set<Coordinate> {
    val basin = mutableSetOf(lowPointLocation)
    val possibleLocations = LinkedList(getNeighbours(lowPointLocation))
    val checked = (possibleLocations + lowPointLocation).toMutableSet()
    while (possibleLocations.isNotEmpty()) {
        val possibleLocation = possibleLocations.poll()
        if (heights.containsKey(possibleLocation) && heights[possibleLocation]!! < 9) {
            basin += possibleLocation
            val newLocations = getNeighbours(possibleLocation).filter { !checked.contains(it) }
            possibleLocations.addAll(newLocations)
            checked.addAll(newLocations)
        }
    }
    return basin
}

private fun findLowPoints(heights: Map<Coordinate, Height>) =
    heights.filter { (coordinate, value) -> allNeighboursAreHigher(coordinate, value, heights)}

private fun allNeighboursAreHigher(
    coordinate: Coordinate,
    value: Height,
    heights: Map<Coordinate, Height>
) = getNeighbours(coordinate)
    .filter { heights.containsKey(it) }
    .all { neighbour -> value < heights[neighbour]!! }

fun getNeighbours(coordinate: Coordinate): Set<Coordinate> {
    return setOf(
        coordinate.move(NORTH),
        coordinate.move(EAST),
        coordinate.move(SOUTH),
        coordinate.move(WEST)
    )
}

typealias Height = Int