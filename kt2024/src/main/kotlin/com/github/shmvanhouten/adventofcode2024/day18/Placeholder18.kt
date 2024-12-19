package com.github.shmvanhouten.adventofcode2024.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate
import com.github.shmvanhouten.adventofcode.utility.grid.boolGridFromCoordinates
import com.github.shmvanhouten.adventofcode.utility.grid.toString
import java.util.*


fun findIfStillTraversable(input: String, startingBytes: Int, maxXAndY: Int): Coordinate {
    val lines = input.lines()
    val result = lines.indices.drop(startingBytes).binarySearchLastWorking { i ->
        hasPath(i, maxXAndY, lines)
    }
    return toCoordinate(lines[result])
}

fun hasPath(nrOfBytes: Int, maxXAndY: Int, lines: List<String>): Boolean {
    val grid =boolGridFromCoordinates(
        lines.take(nrOfBytes).map { toCoordinate(it) }.toSet(),
        maxX=maxXAndY,
        maxY=maxXAndY
    )

    val end = grid.withIndex().last().location
    val bestPathsPerLocation = mutableMapOf<Coordinate, Int>()
    val unfinishedPaths = priorityQueueOf(Path(grid.withIndex().first().location, 0))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, length) = unfinishedPaths.poll()
        if((bestPathsPerLocation[loc] ?: Int.MAX_VALUE) < length) continue

        bestPathsPerLocation[loc] = length
        if(loc == end) return true
        unfinishedPaths.addAll(
            loc.getSurroundingManhattan().map { Path(it, length + 1) }
                .filter { grid.contains(it.loc) && !grid[it.loc] }
                .filter { it.length < (bestPathsPerLocation[end] ?: Int.MAX_VALUE) }
                .filter { (bestPathsPerLocation[it.loc] ?: Int.MAX_VALUE) > it.length }
        )
    }
    return false
}

fun shortestPathAfter(nrOfBytes: Int, maxXAndY: Int, lines: List<String>): Int {
    val grid =boolGridFromCoordinates(
            lines.take(nrOfBytes).map { toCoordinate(it) }.toSet() + fillIn(),
            maxX=maxXAndY,
            maxY=maxXAndY
        )
    println(grid.toString('#', '.'))

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

fun <T> List<T>.binarySearchLastWorking(isWorking: (T) -> Boolean): T {
    var minI = 0
    var maxI = this.size
    while (true) {
        val i = (minI + maxI) / 2
        if(i == minI) return this[minI]
        if(i == maxI) return this[maxI]
        if (isWorking(this[i])) {
            minI = i
        } else {
            maxI = i
        }
    }
}