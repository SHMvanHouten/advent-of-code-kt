package com.github.shmvanhouten.adventofcode2024.day16

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import java.util.*

private const val WALL = '#'
private const val END = 'E'
private const val START = 'S'

fun bestScoreThroughMaze(input: String): Int {
    val bestPaths = bestPathsThroughMaze(input)
    return bestPaths.first().scoreIncurred
}

fun bestSeats(input: String): Int {
    val bestPathsThroughMaze = bestPathsThroughMaze(input)
    return bestPathsThroughMaze.flatMap { it.path }.distinct().size
}


fun bestPathsThroughMaze(input: String): List<Path> {
    val grid = charGrid(input)
    val target = grid.firstLocationOf { it == END }
    val bestPathsPerLocation = mutableMapOf<Pair<Coordinate, Direction>, List<Path>>()
    val unfinishedPaths = priorityQueueOf(Path(grid.firstLocationOf { it == START }!!, Direction.EAST, 0))
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.poll()
        val (loc, dir, score) = path
        val bestPathsSoFar = bestPathsPerLocation[loc to dir]
        if ((bestPathsSoFar?.first()?.scoreIncurred ?: Int.MAX_VALUE) >= score) {
            if (bestPathsSoFar == null || bestPathsSoFar.first().scoreIncurred > score) {
                bestPathsPerLocation[loc to dir] = listOf(path)
            } else {
                bestPathsPerLocation[loc to dir] = bestPathsSoFar + path
            }

            if (loc != target) {

                unfinishedPaths.addAll(
                    possibleDirectionsWithScores(dir)
                        .map { loc.move(it.first) to it }
                        .filter { grid[it.first] != WALL }
                        .map { path.addStep(it.first, it.second.first, it.second.second) }
                )
            }
        }
    }
    val bestPaths = Direction.entries.flatMap { bestPathsPerLocation[target to it]?: emptyList() }
    val bestScore = bestPaths.minOfOrNull { it.scoreIncurred }!!
    return bestPaths.filter { it.scoreIncurred == bestScore }
}

fun possibleDirectionsWithScores(dir: Direction) = listOf(dir.turnLeft() to 1001, dir to 1, dir.turnRight() to 1001)

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

data class Path(
    val location: Coordinate,
    val lastDirection: Direction,
    val scoreIncurred: Int,
    val path: List<Coordinate> = listOf(location)
) : Comparable<Path> {

    override fun compareTo(other: Path): Int = this.scoreIncurred.compareTo(other.scoreIncurred)

    fun addStep(newLoc: Coordinate, newDir: Direction, addedScore: Int): Path =
        copy(
            location = newLoc,
            lastDirection = newDir,
            scoreIncurred = scoreIncurred + addedScore,
            path = path + newLoc
        )
}