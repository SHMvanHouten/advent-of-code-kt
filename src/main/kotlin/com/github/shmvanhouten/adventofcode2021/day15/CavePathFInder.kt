package com.github.shmvanhouten.adventofcode2021.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import java.util.*

fun findLowestRiskPath(riskMap: Map<Coordinate, Int>): Long {
    val endpoint: Coordinate = riskMap.keys.maxByOrNull { it.x + it.y }!!
    val pathsSoFar = LinkedList(listOf(Node(Coordinate(0, 0), 0L)))
    var shortestPath: Long = 918
    while (pathsSoFar.isNotEmpty()) {
        pathsSoFar.sortBy { it.pathLength }
        val (coordinate, pathLength, visitedCoordinates) = pathsSoFar.pollLast()
        val newPaths = getNeighboursInTheRightDirection(coordinate)
            .asSequence()
            .filter { riskMap.contains(it) }
            .map { it to riskMap[it] }
            .filter { !visitedCoordinates.contains(it.first) }
            .map { (c, risk) -> Node(c, pathLength + risk!!, visitedCoordinates + c) }
            .filter { it.pathLength < shortestPath }
            .toList()

        newPaths.find { it.location == endpoint }?.let {
            if(it.pathLength < shortestPath)
            println(it.pathLength)
            shortestPath = it.pathLength
        }

        pathsSoFar += newPaths
    }
    return shortestPath
}

fun getNeighboursInTheRightDirection(coordinate: Coordinate): List<Coordinate> {
    return listOf(
        coordinate.move(Direction.EAST),
        coordinate.move(Direction.SOUTH)
    )
}

data class Node(val location: Coordinate, val pathLength: Long, val visitedCoordinates: Set<Coordinate> = setOf(location))