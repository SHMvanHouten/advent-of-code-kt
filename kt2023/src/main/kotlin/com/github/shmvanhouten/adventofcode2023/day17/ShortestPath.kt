package com.github.shmvanhouten.adventofcode2023.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.SOUTH
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.pairs.nullablePair
import java.util.*

fun Grid<Int>.coolestPath(): Int {
    return bestPath{this.take(3)}
}

fun Grid<Int>.ultraPath(): Int {
    return bestPath { drop(3).take(7) }
}

private fun Grid<Int>.bestPath(takeRule: (Sequence<Pair<Coordinate, Int>>).() -> Sequence<Pair<Coordinate, Int>>): Int {
    val quickestPathTo = mutableMapOf(Coordinate(0, 0) to mutableMapOf<Direction, Int>())
    val unfinishedPaths = LinkedList(listOf(Path(Coordinate(0, 0), 0, listOf(EAST, SOUTH))))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, incurredHeat, allowedDirections) = unfinishedPaths.poll()
        val paths = quickestPathTo.getOrPut(loc) { mutableMapOf() }

        allowedDirections.filter { !paths.contains(it) || incurredHeat < paths[it]!! }
            .forEach { dir ->
                quickestPathTo[loc]!![dir] = incurredHeat

                generateSequence(nullablePair(loc.move(dir), incurredHeat + this[loc])) { (l, heat) ->
                    nullablePair(l.move(dir), this.getOrNull(l)?.plus(heat))
                }
                    .takeWhile { this.contains(it.first) }
                    .takeRule()
                    .forEach { (l, heat) ->
                        unfinishedPaths.add(Path(l, heat, listOf(dir.turnLeft(), dir.turnRight())))
                    }
            }
    }
    val quickestPath = quickestPathTo[bottomRightLocation]?.minOf { it.value } ?: error("did not find path")
    return quickestPath + this.last() - this.first()
}

data class Path(val location: Coordinate, val heatIncurred: Int, val allowedDirections: List<Direction>)