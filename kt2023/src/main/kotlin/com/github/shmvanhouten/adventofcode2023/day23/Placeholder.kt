package com.github.shmvanhouten.adventofcode2023.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    readFile("/input-day23.txt")
        .lines()
        .onEach(::println)
}

fun longestPath(input: String): Int {
    val grid = charGrid(input)
    val goal = grid.lastCoordinateMatching { it == '.' }!!
    val start = grid.firstCoordinateMatching { it == '.' }!!
    val finishedPaths = mutableSetOf<Path>()
    val unfinished = mutableSetOf(Path(setOf(start)))
    while (unfinished.isNotEmpty()) {
        val path = unfinished.last()
        unfinished.remove(path)
        val currentStep = path.last()
        val char = grid[currentStep]
        if (char.isSlope()) {
            val slipped = currentStep.moveInDirection(char)
            if(slipped !in path.steps) unfinished += path + slipped
        } else if(currentStep == goal) finishedPaths += path
        else {
            unfinished += currentStep.getSurroundingManhattan()
                .filter { grid.contains(it) && grid[it] != '#' }
                .filter { it !in path.steps }
                .map { path + it }
        }

    }
    return finishedPaths.maxOf { it.steps.size } - 1
}

private fun Char.isSlope(): Boolean = this != '.'

private fun Coordinate.moveInDirection(char: Char): Coordinate = when(char) {
    '>' -> this.move(EAST)
    'v' -> this.move(SOUTH)
    '<' -> this.move(WEST)
    '^' -> this.move(NORTH)
    else -> error("unknown char $char")
}

data class Path(val steps: Set<Coordinate>) {
    fun last(): Coordinate = steps.last()
    operator fun plus(loc: Coordinate): Path {
        return Path(steps + loc)
    }
}
