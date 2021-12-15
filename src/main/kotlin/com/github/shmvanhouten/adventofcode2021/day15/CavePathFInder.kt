package com.github.shmvanhouten.adventofcode2021.day15

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.day09.getNeighbours
import java.util.*

val origin = Coordinate(0,0)

//fun findLowestRiskPath(riskMap: Map<Coordinate, Int>): Long {
//    val endpoint: Coordinate = riskMap.keys.maxByOrNull { it.x + it.y }!!
//    val riskPathByCoordinate = mutableMapOf(origin to 0)
//
//    for (coordinate in riskMap.keys.minus(origin).sortedWith(CoordinateComparator())) {
//
//    }
//}
//
//class CoordinateComparator : Comparator<Coordinate> {
//    override fun compare(one: Coordinate?, other: Coordinate?): Int {
//        if(one == other) return 0
//        if(one == null) return 1
//        if(other == null) return -1
//        val compareTo = (one.x + one.y).compareTo(other.x + other.y)
//        return if(compareTo == 0) one.x.compareTo(other.x)
//        else compareTo
//    }
//
//}

fun findLowestRiskPath(riskMap: Map<Coordinate, Int>): Int {
    val endpoint: Coordinate = riskMap.keys.maxByOrNull { it.x + it.y }!!
    val pathsSoFar = LinkedList(listOf(Node(origin, 0)))
    val visitedCoordinates = mutableMapOf<Coordinate, Int>()
    var shortestPath = 572
    while (pathsSoFar.isNotEmpty()) {
        pathsSoFar.sortWith(NodeComparator())
        val (coordinate, pathLength, _) = pathsSoFar.pollLast()
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
            println(it.pathLength)
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

data class Node(val location: Coordinate, val pathLength: Int, val visitedCoordinates: Set<Coordinate> = setOf(location))