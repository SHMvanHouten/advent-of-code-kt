package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.collections.arrayDequeOf
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap

fun shortestPath(input: String): Path {
    val coords = input.toCoordinateMap()
    return shortestPathBackward(coords) {
        current.coord == coords.entries.first { it.value == 'S' }.key
    }
}

fun shortestPathFromAnyATile(input: String): Path {
    return shortestPathBackward(input.toCoordinateMap()) { current.height == 'a' }
}

private fun shortestPathBackward(coords: Map<Coordinate, Char>, finishCriterium: Path.() -> Boolean): Path {
    return shortestPathBackward(
        coordinatesWithStartAndEndAsHeights(coords),
        finishCriterium,
        Point(coords.entries.first { it.value == 'E' }.key, height = 'z')
    )
}

fun shortestPathBackward(
    coordinates: Map<Coordinate, Char>,
    hasFinished: Path.() -> Boolean,
    start: Point
): Path {
    val paths = arrayDequeOf(Path(start))
    val shortestPathByCoordinate = mutableMapOf(start.coord to 0)

    while (paths.isNotEmpty()) {
        val path = paths.removeFirst()
        if (path.hasFinished()) return path

        val current = path.current.coord
        current.getSurroundingManhattan()
            .mapNotNull { nullablePoint(it, coordinates[it]) }
            .filter { (_, height) -> height + 1 >= path.current.height }
            .map { path + it }
            .filter { !shortestPathByCoordinate.contains(it.current.coord) }
            .forEach { p ->
                shortestPathByCoordinate += p.current.coord to p.length
                paths += p
            }
    }

    error("no paths found!")
}

private fun coordinatesWithStartAndEndAsHeights(coords: Map<Coordinate, Char>) =
    coords
        .mapValues { if (it.value == 'E') 'z' else it.value }
        .mapValues { if (it.value == 'S') 'a' else it.value }

data class Point(val coord: Coordinate, val height: Height)

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
