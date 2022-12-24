package com.github.shmvanhouten.adventofcode2022.day24

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import java.util.*

private const val minX = 0
private const val minY = 0

fun thereAndBackAndThere(input: String): Int {
    val blizzardMap = BlizzardMap(input)
    val (there, blizzardMap1) = fastestPathThroughBlizzard(blizzardMap)
    val (back, blizzardMap2) = fastestPathThroughBlizzard(
        blizzardMap1,
        start = there.current,
        target = Coordinate(1,0),
        minute = there.minute
        )
    val (thereAgain, _) = fastestPathThroughBlizzard(
        blizzardMap2,
        minute = back.minute
    )
    return thereAgain.minute
}

fun fastestPathThroughBlizzard(input: String): Pair<Path, BlizzardMap> {
    val blizzardMap = BlizzardMap(input)
    return fastestPathThroughBlizzard(blizzardMap)
}

private fun fastestPathThroughBlizzard(
    blizzardMap: BlizzardMap,
    start: Coordinate = Coordinate(1, 0),
    target: Coordinate = targetLocation(blizzardMap),
    minute: Int = 0
): Pair<Path, BlizzardMap> {
    val (maxX, maxY) = blizzardMap.getState(0).bottomRight
    val paths = priorityQueueOf(Path(start, minute))
    var shortestPath: Path? = null
    val visited = mutableSetOf<Path>()

    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if (path.current == target) {
            if (shortestPath == null || path.minute < shortestPath.minute) {
                shortestPath = path
                println("found path")
                println(path)
            }
        } else if (path.minute > (shortestPath?.minute ?: (minute + 1000))) continue
        else if (visited.contains(path)) continue
        else {
            visited += path
            val nextBlizzardState = blizzardMap.getState(path.minute + 1)
            (path.current.getSurroundingManhattan() + path.current)
                .filter { it == target || it == start || it.x in (minX + 1) until maxX && it.y in minY + 1 until maxY }
                .filter { nextBlizzardState.hasNoBlizzardAt(it) }
                .map { path.moveTo(it) }
                .forEach { paths += it }
        }
    }
    if (shortestPath == null) error("no path found")
    return shortestPath to blizzardMap
}

fun targetLocation(blizzardMap: BlizzardMap): Coordinate {
    val bottomRight = blizzardMap.getState(0).bottomRight
    return bottomRight.copy(x = bottomRight.x - 1)
}

fun priorityQueueOf(path: Path): PriorityQueue<Path> {
    val queue = PriorityQueue(PathComparator())
    queue.add(path)
    return queue
}

data class Path(
    val current: Coordinate,
    val minute: Int,
//    val previous: List<Coordinate>
) {

    val value: Int = current.value - minute

    fun moveTo(nextLocation: Coordinate): Path {
        return this.copy(
            nextLocation,
            minute + 1,
//            previous + current
        )
    }

}

class PathComparator : Comparator<Path> {
    override fun compare(one: Path?, other: Path?): Int {
        if (one == null || other == null) error("null paths")
        return (other.current).compareTo(one.current)
    }

}

private fun Coordinate.compareTo(other: Coordinate): Int {
    return (x + y).compareTo(other.x + other.y)
}

private val Coordinate.value: Int
    get() {
        return x + y
    }