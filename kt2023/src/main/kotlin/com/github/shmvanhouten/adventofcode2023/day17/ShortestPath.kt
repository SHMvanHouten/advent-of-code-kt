package com.github.shmvanhouten.adventofcode2023.day17

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.SOUTH
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.pairs.nullablePair
import java.util.*

fun Grid<Int>.bestPath(): Int {
    val quickestPathTo = mutableMapOf(Coordinate(0, 0) to mutableMapOf(EAST to 1, SOUTH to 0))
    val unfinishedPaths = LinkedList(listOf(Path(Coordinate(0, 0), 0, listOf(EAST, SOUTH))))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, incurredHeat, allowedDirections) = unfinishedPaths.poll()
        if(!this.contains(loc)) continue
        val paths = quickestPathTo[loc]
        val directionsToGo = if(paths != null) {
            allowedDirections.filter { !paths.contains(it) || paths[it]!! > incurredHeat }
        } else {
            quickestPathTo[loc] = mutableMapOf<Direction, Int>()
            allowedDirections
        }

        directionsToGo.forEach { dir ->
            quickestPathTo[loc]!![dir] = incurredHeat
            generateSequence(nullablePair(loc.move(dir), incurredHeat + this[loc])) { (l, heat) ->
                 nullablePair(l.move(dir), this.getOrNull(l)?.plus(heat))
            }.take(3)
                .forEach { (l, heat) ->
                    unfinishedPaths.add(Path(l, heat, listOf(dir.turnLeft(), dir.turnRight())))
                }
        }
    }
    val quickestPath = quickestPathTo[this.bottomRight()]?.minOf { it.value } ?: error("did not find path")
    return quickestPath + this[bottomRight()] - this[Coordinate(0, 0)]
}

private fun <T> Grid<T>.bottomRight(): Coordinate {
    return Coordinate(this.width - 1, this.height - 1)
}

data class Path(val location: Coordinate, val heatIncurred: Int, val allowedDirections: List<Direction>)