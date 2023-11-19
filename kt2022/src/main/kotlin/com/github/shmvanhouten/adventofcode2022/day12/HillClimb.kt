package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.collections.arrayDequeOf
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun shortestPath(input: String): Path {
    val grid = charGrid(input)
    return shortestPathBackward(grid) {
        current.coord == grid.firstCoordinateMatching { it == 'S' }
    }
}

fun shortestPathFromAnyATile(input: String): Path {
    return shortestPathBackward(charGrid(input)) { current.height == 'a' }
}

private fun shortestPathBackward(coords: Grid<Char>, finishCriterion: Path.() -> Boolean): Path {
    return shortestPathBackward(
        replaceStartAndEndWithHeights(coords),
        finishCriterion,
        Point(coords.firstCoordinateMatching { it == 'E' }!!, height = 'z')
    )
}

fun shortestPathBackward(
    grid: Grid<Char>,
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
            .mapNotNull { nullablePoint(it, grid.getOrNull(it)) }
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

private fun replaceStartAndEndWithHeights(coords: Grid<Char>): Grid<Char> =
    coords
        .replaceElements('E', replacement = 'Z')
        .replaceElements('S', replacement = 'a')

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
