package com.github.shmvanhouten.adventofcode2021.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.day09.getNeighbours
import java.util.*

val origin = Coordinate(0, 0)

fun findLowestRiskPath(riskMap: Map<Coordinate, Int>): Path {
    val endpoint: Coordinate = riskMap.keys.maxByOrNull { it.x + it.y }!!
    val pathsSoFar = priorityQueueOf(Node(origin))
    val visitedCoordinates = mutableMapOf<Coordinate, Int>()
    while (pathsSoFar.isNotEmpty()) {
        val currentNode = pathsSoFar.poll()
        val (coordinate, pathLength) = currentNode
        val newPaths = getNeighbours(coordinate)
            .asSequence()
            .filter { riskMap.contains(it) }
            .map { it to riskMap[it]!! }
            .map { (c, risk) -> Node(c, pathLength + risk, currentNode) }
            .filter { !visitedCoordinates.contains(it.location) || visitedCoordinates[it.location]!! > it.pathLength }
            .toList()

        newPaths.find { it.location == endpoint }?.let {
            return it.buildPath()
        }

        visitedCoordinates += newPaths.map { it.location to it.pathLength }
        pathsSoFar += newPaths
    }
    error("no shortest path found!")
}

private fun priorityQueueOf(node: Node): PriorityQueue<Node> {
    val priorityQueue = PriorityQueue(NodeComparator())
    priorityQueue.add(node)
    return priorityQueue
}

class NodeComparator : Comparator<Node> {
    override fun compare(one: Node?, other: Node?): Int {
        val compareTo: Int = (one?.pathLength ?: -1).compareTo(other?.pathLength ?: -1)
        return if (compareTo == 0) {
            whicheverIsClosestToTheEnd(one, other)
        } else {
            compareTo
        }
    }

    private fun whicheverIsClosestToTheEnd(
        one: Node?,
        other: Node?
    ) = (one?.location?.x?.plus(one.location.y) ?: -1).compareTo(other?.location?.x?.plus(other.location.y) ?: -1)

}

data class Node(
    val location: Coordinate,
    val pathLength: Int = 0,
    val previousNode: Node? = null
) {
    fun buildPath(): Path {
        return Path(
            generateSequence(this) { node -> node.previousNode }.toList(),
            this.pathLength
        )
    }
}

data class Path(val nodes: List<Node>, val length: Int)
