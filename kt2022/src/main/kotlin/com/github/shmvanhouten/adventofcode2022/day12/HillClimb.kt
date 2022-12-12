package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
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

    val paths = priorityQueueOf(Path(Point(start, 'a')))
    var shortestPath: Path? = null
    val shortestPathByCoordinate = mutableMapOf(start to 0)
    if(shortestPathSoFar.isNotEmpty()) {
        shortestPath = shortestPathSoFar
        shortestPathByCoordinate += shortestPathSoFar.current.coord to shortestPathSoFar.length
    }
    while (paths.isNotEmpty()) {
        val path = paths.poll()
        val current = path.current.coord
        current.getSurroundingManhattan()
            .mapNotNull { nullablePoint(it, coordinates[it]) }
            .filter { (_, height) -> height <= path.current.height + 1 }
            .map { path + it }
            .filter { it.length < (shortestPathByCoordinate[it.current.coord] ?: Int.MAX_VALUE) }
            .forEach { p ->
                shortestPathByCoordinate += p.current.coord to p.length
                if (p.current.coord == end) shortestPath = p
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
    return Path(Point(Coordinate(0, 0), '~'))
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

data class Point(val coord: Coordinate, val height: Height) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Point
        return coord == other.coord
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}

fun nullablePoint(coord: Coordinate, height: Height?): Point? {
    return if (height == null) null
    else Point(coord, height)
}

data class Path(
    val current: Point,
    val length: Int = 0,
    val stepsBefore: Path? = null
) {
    fun isNotEmpty(): Boolean {
        return stepsBefore != null
    }

    operator fun plus(nextStep: Point): Path {
        return Path(
            nextStep, length + 1, this
        )
    }

}

typealias Height = Char
