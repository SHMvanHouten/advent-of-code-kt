package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode.utility.pairs.nullablePair
import java.util.Comparator
import java.util.PriorityQueue

fun shortestPath(input: String): Path {
    val coords = input.toCoordinateMap()
    val coordinates = coordinatesWithStartAndEndAsHeights(coords)
    return shortestPath(
        coordinates,
        coords.entries.first { it.value == 'S' }.key,
        coords.entries.first { it.value == 'E' }.key
    )
}

fun shortestPathFromAnyATile(input: String): Path {
    val coords = input.toCoordinateMap()
    val end = coords.entries.first { it.value == 'E' }.key
    val coordinates = coordinatesWithStartAndEndAsHeights(coords)

    return coordinates.filter { it.value == 'a' }
        .keys
        .fold(emptyPath()) { acc, start -> shortestPath(coordinates, start, end, acc) }
}

fun shortestPath(coordinates: Map<Coordinate, Char>, start: Coordinate, end: Coordinate, shortestPathSoFar: Path = emptyPath()): Path {
    val paths = priorityQueueOf(Path(mutableSetOf(start), currentHeight = 'a'))
    var shortestPath: Path? = null
    val shortestPathByCoordinate = mutableMapOf(start to 0)
    if(shortestPathSoFar.isNotEmpty()) {
        shortestPath = shortestPathSoFar
        shortestPathByCoordinate += shortestPathSoFar.current to shortestPathSoFar.length
    }
    while (paths.isNotEmpty()) {
        val path = paths.poll()
        val current = path.current
        current.getSurroundingManhattan()
            .filter { !path.contains(it) }
            .mapNotNull { nullablePair(it, coordinates[it]) }
            .filter { (_, height) -> height <= path.currentHeight + 1 }
            .map { path + it }
            .filter { it.length < (shortestPathByCoordinate[it.current] ?: Int.MAX_VALUE) }
            .forEach { p ->
                shortestPathByCoordinate += p.current to p.length
                if (p.current == end) shortestPath = p
                else paths += p
            }
    }
    return shortestPath?: error("no paths found!")
}

private fun coordinatesWithStartAndEndAsHeights(coords: Map<Coordinate, Char>) =
    coords
        .mapValues { if (it.value == 'E') 'z' else it.value }
        .mapValues { if (it.value == 'S') 'a' else it.value }

fun emptyPath(): Path {
    return Path(emptySet(), Coordinate(0, 0), 'Z')
}

private fun priorityQueueOf(path: Path): PriorityQueue<Path> {
    val queue = PriorityQueue(PathComparator())
    queue.add(path)
    return queue
}

class PathComparator: Comparator<Path> {
    override fun compare(one: Path?, other: Path?): Int {
        if (one == null || other == null) error("paths are null?")
        return one.length.compareTo(other.length)
    }

}

data class Path(
    val coordinates: Set<Coordinate>,
    val current: Coordinate = coordinates.last(),
    val currentHeight: Char
) {
    val length: Int = coordinates.size - 1

    fun contains(it: Coordinate): Boolean {
        return coordinates.contains(it)
    }

    fun isNotEmpty(): Boolean {
        return coordinates.isNotEmpty()
    }

    operator fun plus(nextStep: Pair<Coordinate, Char>): Path {
        return Path(
            coordinates + nextStep.first,
            current = nextStep.first,
            currentHeight = nextStep.second
        )
    }

}
