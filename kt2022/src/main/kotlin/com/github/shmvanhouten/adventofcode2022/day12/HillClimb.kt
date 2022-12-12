package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import java.util.Comparator
import java.util.PriorityQueue

fun shortestPath(input: String): Set<Coordinate> {
    val coords = input.toCoordinateMap()
    val coordinates = coords
        .mapValues { if(it.value == 'E') 'z' else it.value }
        .mapValues { if(it.value == 'S') 'a' else it.value }
    return shortestPath(
        coordinates,
        coords.entries.first { it.value == 'S' }.key,
        coords.entries.first { it.value == 'E' }.key
    )
}

fun shortestPathFromAnyATile(input: String): Set<Coordinate> {
    val coords = input.toCoordinateMap()
    val end = coords.entries.first { it.value == 'E' }.key
    val coordinates = coords
        .mapValues { if(it.value == 'E') 'z' else it.value }
        .mapValues { if(it.value == 'S') 'a' else it.value }
    return coordinates.filter { it.value == 'a' }
        .keys
        .map { shortestPath(coordinates, it, end) }
        .filter { it.isNotEmpty() }
        .minByOrNull { it.size }?: error("no shortest path found!")
}

fun shortestPath(coordinates: Map<Coordinate, Char>, start: Coordinate, end: Coordinate): Set<Coordinate> {
    val paths = priorityQueueOf(Path(mutableSetOf(start), currentHeight = 'a'))
    val finished: MutableSet<Path> = mutableSetOf()
    val visitedCoordinatesShortestPath = mutableMapOf(start to 1)
    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if(finished.isNotEmpty() && path.size > finished.last().size) {
            // do nothing
        } else {
            val current = path.current
            current.getSurrounding()
                .asSequence()
                .filter { !path.contains(it) }
                .map { it to coordinates[it] }
                .filter { it.second != null }.map { (a, b) -> a to b!! }
                .filter { (_, height) -> height <= path.currentHeight + 1 }
                .map { path.add(it) }
                .filter { it.size < (visitedCoordinatesShortestPath[it.current] ?: Int.MAX_VALUE) }
                .toList()
                .forEach { p ->
                    visitedCoordinatesShortestPath += p.current to p.size
                    if (p.current == end) {
                        println(path.size)
                        finished += p
                    } else paths += p
                }
        }
    }
    return finished.map { it.coordinates }.minByOrNull { it.size }?: emptySet()
}

fun priorityQueueOf(path: Path): PriorityQueue<Path> {
    val queue = PriorityQueue(PathComparator())
    queue.add(path)
    return queue
}

class PathComparator: Comparator<Path> {
    override fun compare(one: Path?, other: Path?): Int {
        if (one == null || other == null) error("paths are null?")
        return one.size.compareTo(other.size)
    }

}

data class Path(
    val coordinates: Set<Coordinate>,
    val size: Int = coordinates.size,
    val current: Coordinate = coordinates.last(),
    val currentHeight: Char
) {
    fun contains(it: Coordinate): Boolean {
        return coordinates.contains(it)
    }

    fun add(nextStep: Pair<Coordinate, Char>): Path {
        return Path(
            coordinates + nextStep.first,
            current = nextStep.first,
            currentHeight = nextStep.second
        )
    }

}
