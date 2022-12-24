package com.github.shmvanhouten.adventofcode2022.day24

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import java.util.*
import kotlin.math.max

private const val MIN_X = 0
private const val MIN_Y = 0

fun thereAndBackAndThere(input: String): Int {
    val blizzardStates = BlizzardMap(input)
    return moveThroughBlizzard(blizzardStates)
        .let { moveThroughBlizzard(blizzardStates, start = it.current, target = Coordinate(1, 0), time = it.time) }
        .let { moveThroughBlizzard(blizzardStates, time = it.time) }
        .time
}

fun moveThroughBlizzard(input: String): Path {
    val blizzardMap = BlizzardMap(input)
    return moveThroughBlizzard(blizzardMap)
}

private fun moveThroughBlizzard(
    blizzardStates: BlizzardMap,
    start: Coordinate = Coordinate(1, 0),
    target: Coordinate = exitLocation(blizzardStates),
    time: Minute = 0
): Path {
    val paths = priorityQueueOf(Path(start, time))
    var shortestPath: Path? = null
    val visited = mutableSetOf<Path>()

    while (paths.isNotEmpty()) {
        val path = paths.poll()
        when {
            path.current == target -> {
                if (shortestPath == null || path.time < shortestPath.time) {
                    shortestPath = path
                }
            }

            path.time > (shortestPath?.time ?: (time + 1000)) -> continue
            visited.contains(path) -> continue
            else -> {
                visited += path
                val nextBlizzardState = blizzardStates[path.time + 1]
                paths += (path.current.getSurroundingManhattan() + path.current)
                    .filter { isInBounds(it, target, start) }
                    .filter { nextBlizzardState.hasNoBlizzardAt(it) }
                    .map { path.moveTo(it) }
            }
        }
    }
    return shortestPath ?: error("no path found")
}

private fun isInBounds(
    loc: Coordinate,
    target: Coordinate,
    start: Coordinate
): Boolean {
    val xRange = (MIN_X + 1) until max(target.x, start.x) + 1
    val yRange = MIN_Y + 1 until max(target.y, start.y)
    return loc == target ||
            loc == start ||
            loc.x in xRange && loc.y in yRange
}

private fun exitLocation(blizzardMap: BlizzardMap): Coordinate {
    val bottomRight = blizzardMap[0].bottomRight
    return bottomRight.copy(x = bottomRight.x - 1)
}

private fun priorityQueueOf(path: Path): PriorityQueue<Path> {
    val queue = PriorityQueue(PathComparator())
    queue.add(path)
    return queue
}

data class Path(
    val current: Coordinate,
    val time: Minute
) {
    val value: Int = current.x + current.y - time

    fun moveTo(nextLocation: Coordinate): Path {
        return this.copy(
            current = nextLocation,
            time = time + 1
        )
    }

}

class PathComparator : Comparator<Path> {
    override fun compare(one: Path?, other: Path?): Int {
        if (one == null || other == null) error("null paths")
        return (other.value).compareTo(one.value)
    }

}

typealias Minute = Int