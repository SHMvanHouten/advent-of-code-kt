package com.github.shmvanhouten.adventofcode2023.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.SOUTH
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import java.util.*

fun Grid<Int>.coolestPath(): Int = bestPath { this.take(3) }

fun Grid<Int>.ultraPath(): Int = bestPath { drop(3).take(7) }

private val allowedTurns = Direction.entries.associateWith { listOf(it.turnLeft(), it.turnRight()) }

private fun Grid<Int>.bestPath(takeRule: (Sequence<Pair<Coordinate, Int>>).() -> Sequence<Pair<Coordinate, Int>>): Int {
    val quickestPathTo = mutableMapOf(Coordinate(0, 0) to mutableMapOf<Direction, Int>())
    val unfinishedPaths = priorityQueueOf(Path(Coordinate(0, 0), 0, listOf(EAST, SOUTH)))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, incurredHeat, allowedDirections) = unfinishedPaths.poll()
        val paths = quickestPathTo.getOrPut(loc) { mutableMapOf() }

        allowedDirections.filter { !paths.contains(it) || incurredHeat < paths[it]!!}
            .forEach { dir ->
                quickestPathTo[loc]!![dir] = incurredHeat

                generateSequence(loc.move(dir) to incurredHeat + this[loc]) { (next, heat) ->
                    next.move(dir) to this[next] + heat
                }
                    .takeWhile { this.contains(it.first) }
                    .takeRule()
                    .forEach { (l, heat) ->
                        unfinishedPaths.add(Path(l, heat, allowedTurns[dir]!!))
                    }
            }
    }
    val quickestPath = quickestPathTo[bottomRightLocation]?.minOf { it.value } ?: error("did not find path")
    return quickestPath + this.last() - this.first()
}

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

data class Path(val location: Coordinate, val heatIncurred: Int, val allowedDirections: List<Direction>): Comparable<Path> {
    override fun compareTo(other: Path): Int = this.heatIncurred.compareTo(other.heatIncurred)
}