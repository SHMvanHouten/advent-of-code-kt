package com.github.shmvanhouten.adventofcode2021.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.day09.getNeighbours
import java.util.*

val origin = Coordinate(0,0)

fun findLowestRiskPath(riskMap: Map<Coordinate, Int>): Int {
    val endpoint: Coordinate = riskMap.keys.maxByOrNull { it.x + it.y }!!
    val pathsSoFar = PriorityQueue(NodeComparator())
    pathsSoFar.add(Node(origin, 0))
    val visitedCoordinates = mutableMapOf<Coordinate, Int>()
    var shortestPath = Int.MAX_VALUE
    while (pathsSoFar.isNotEmpty()) {
        val (coordinate, pathLength, _) = pathsSoFar.poll()
        val newPaths = getNeighbours(coordinate)
            .asSequence()
            .filter { riskMap.contains(it) }
            .map { it to riskMap[it] }
            .map { (c, risk) -> Node(c, pathLength + risk!!, emptySet()) }
            .filter { it.pathLength + (endpoint - it.location).minimumValue() < shortestPath }
            .filter { !visitedCoordinates.contains(it.location) || visitedCoordinates[it.location]!! > it.pathLength }
            .toList()

        newPaths.find { it.location == endpoint }?.let {
            if(it.pathLength < shortestPath)
            shortestPath = it.pathLength
        }

        visitedCoordinates += newPaths.map { it.location to it.pathLength }
        pathsSoFar += newPaths
    }
    return shortestPath
}

private fun Coordinate.minimumValue(): Int {
    return this.x + this.y
}

class NodeComparator: Comparator<Node> {
    override fun compare(one: Node?, other: Node?): Int {
        val compareTo: Int = (one?.pathLength?:-1).compareTo(other?.pathLength?:-1)
        return if(compareTo == 0) {
            (one?.location?.x?.plus(one.location.y)?:-1).compareTo(other?.location?.x?.plus(other.location.y) ?: -1)
        } else {
            compareTo
        }
    }

}

data class Node(
    val location: Coordinate,
    val pathLength: Int,
    val visitedCoordinates: Set<Coordinate> = setOf(location) // todo: use previous Node instead
)