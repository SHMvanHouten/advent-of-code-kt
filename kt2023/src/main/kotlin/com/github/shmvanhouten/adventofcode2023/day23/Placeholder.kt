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
    val finishedPaths = mutableSetOf<Path>()
    val unfinished = mutableSetOf(Path(emptySet(), Coordinate(1, 0), Coordinate(1, 1), 1))
    while (unfinished.isNotEmpty()) {
        val path = unfinished.last()
        unfinished.remove(path)
        var previousStep = path.previous
        var currentStep: Coordinate? = path.last
        var size = path.size
        while (currentStep != null) {
//            val char = grid[currentStep]
//            if (char.isSlope()) {
//                val slipped = currentStep.moveInDirection(char)
//                if(slipped == path.previous) currentStep = null
//                else {
//                    size++
//                    previousStep = currentStep
//                    currentStep = slipped
//                }
//            } else
            if (currentStep == goal) {
                finishedPaths += path.copy(size = size)
                currentStep = null
            } else {
                val nextSteps = currentStep.getSurroundingManhattan()
                    .filter { grid[it] != '#' }
                    .filter { it != previousStep }
                    .filter { it !in path.memorableSteps }
                if (nextSteps.size > 1) {// is branching path
                    unfinished += nextSteps.map {
                        Path(path.memorableSteps + currentStep!!, currentStep!!, it, size + 1)
                    }
                    currentStep = null
                } else if(nextSteps.isEmpty()) {
                    currentStep = null
                } else {
                    size += 1
                    previousStep = currentStep
                    currentStep = nextSteps.first()
                }

            }
        }

    }
    return finishedPaths.maxOf { it.size }
}

private fun Char.isSlope(): Boolean = this != '.'

private fun Coordinate.moveInDirection(char: Char): Coordinate = when(char) {
    '>' -> this.move(EAST)
    'v' -> this.move(SOUTH)
    '<' -> this.move(WEST)
    '^' -> this.move(NORTH)
    else -> error("unknown char $char")
}

data class Path(val memorableSteps: Set<Coordinate>, val previous: Coordinate, val last: Coordinate, val size: Int) {

}
