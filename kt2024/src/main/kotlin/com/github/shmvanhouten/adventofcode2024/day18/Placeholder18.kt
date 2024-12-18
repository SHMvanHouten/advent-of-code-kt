package com.github.shmvanhouten.adventofcode2024.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromCoordinates
import java.util.*

fun shortestPathAfter(input: String, nrOfBytes: Int, maxXAndY: Int): Int {
    val grid =boolGridFromCoordinates(
            input.lines().take(nrOfBytes).map { toCoordinate(it) }.toSet() + fillIn(),
            maxX=maxXAndY,
            maxY=maxXAndY
        )
    println(grid.map { if(it) '#' else '.' })

    val end = grid.withIndex().last().location
    val bestPathsPerLocation = mutableMapOf<Coordinate, Int>()
    val unfinishedPaths = priorityQueueOf(Path(grid.withIndex().first().location, 0))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, length) = unfinishedPaths.poll()
        if((bestPathsPerLocation[loc] ?: Int.MAX_VALUE) < length) continue

        bestPathsPerLocation[loc] = length
        if(loc == end) continue
        unfinishedPaths.addAll(
            loc.getSurroundingManhattan().map { Path(it, length + 1) }
                .filter { grid.contains(it.loc) && !grid[it.loc] }
                .filter { it.length < (bestPathsPerLocation[end] ?: Int.MAX_VALUE) }
                .filter { (bestPathsPerLocation[it.loc] ?: Int.MAX_VALUE) > it.length }
        )
    }
    return bestPathsPerLocation[end]!!
}

fun fillIn(): Set<Coordinate> {
    return (58..70).flatMap { y ->
        (0..30).map { Coordinate(it, y) }
    }.toSet() + (0..36).flatMap { y ->
        (46..70).map { Coordinate(it, y) }
    }.toSet()
}

data class Path(val loc: Coordinate, val length: Int): Comparable<Path> {
    override fun compareTo(other: Path): Int = other.length.compareTo(this.length)
}

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))
