package com.github.shmvanhouten.adventofcode2024.day16

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import java.util.*

private const val WALL = '#'

fun bestWayThroughMaze(input: String): Int {
    val grid = charGrid(input)
    val target = grid.firstLocationOf { it == 'E' }
    val scorePerLocation = mutableMapOf<Coordinate, Int>()
    val unfinishedPaths = priorityQueueOf(Path(grid.firstLocationOf { it == 'S' }!!, Direction.EAST, 0))
    while (unfinishedPaths.isNotEmpty()) {
        val (loc, dir, score) = unfinishedPaths.poll()
        if((scorePerLocation[loc] ?: Int.MAX_VALUE) >= score) {
            scorePerLocation[loc] = score

            if(loc != target) {

                unfinishedPaths.addAll(
                    possibleDirections(dir)
                        .map { loc.move(it.first) to it }
                        .filter { grid[it.first] != WALL }
                        .map { Path(it.first, it.second.first, score + it.second.second) }
                )
            }
        }
    }
    return scorePerLocation[target]!!
}

fun possibleDirections(dir: Direction) = listOf(dir.turnLeft() to 1001, dir to 1, dir.turnRight() to 1001)

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

data class Path(val location: Coordinate, val lastDirection: Direction, val scoreIncurred: Int): Comparable<Path> {
    override fun compareTo(other: Path): Int = this.scoreIncurred.compareTo(other.scoreIncurred)
}