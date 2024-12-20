package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import java.util.*

fun findCheats(input: String): List<CheatingResult> {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    val quickestPath = quickestPath(grid, start, goal)!!
    return possibleCheatsAlongPath(grid, quickestPath)
        .map { it to grid.applyCheat(it) }
        .mapNotNull { (cheat, grid) ->
            val pathFromCheat = quickestPath(grid, cheat.removedWalls.first(), goal, quickestPath)
            val index = quickestPath.steps.indexOf(cheat.stepToBlock)
            if(pathFromCheat  != null){
                val fullPath = Path(quickestPath.steps.subList(0, index) + pathFromCheat.steps)
                toCheatingResult(cheat, grid, fullPath, quickestPath.length)
            } else null
        }
}

fun possibleCheatsAlongPath(grid: Grid<Char>, quickestPath: Path): List<Cheat> {
    val steps = quickestPath.steps
    return steps.subList(0, steps.lastIndex).asSequence()
        .flatMap { step -> adjacentInnerWalls(step, grid).map { step to it } }
        .map { (step, wall) -> Cheat(listOf(wall), steps[steps.indexOf(step) + 1]) }
        .toList()
}

fun adjacentInnerWalls(loc: Coordinate, grid: Grid<Char>): List<Coordinate> {
    return loc.getSurroundingManhattan()
        .filter { grid[it] == '#' }
        .filter { !grid.isOnPerimiter(it) } // todo: can be more efficient
}

fun quickestPath(input: String): Path {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    return quickestPath(grid, start, goal)!!
}

private fun quickestPath(
    grid: Grid<Char>,
    start: Coordinate,
    goal: Coordinate,
    existingPath: Path = emptyPath()
): Path? {
    val unfinishedPaths = priorityQueueOf(Path(listOf(start)))
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.poll()
        val lastLoc = path.lastLoc
        if(existingPath.isNotEmpty() && path.length >= existingPath.length) return null
//        if(lastLoc in existingPath.steps) return path.dropLast() + existingPath.pathFrom(lastLoc)
        if (lastLoc == goal) return path
        unfinishedPaths.addAll(path.nextSteps(grid)
            .map { path.stepTo(it) })
    }
    return null
}

fun emptyPath(): Path {
    return Path(emptyList())
}

data class Path(
    val steps: List<Coordinate>
): Comparable<Path> {
    val length = steps.size - 1
    val lastLoc = steps.lastOrNull()?:Coordinate(-1,-1)
    private val beforeLastLoc = steps.getOrNull(steps.lastIndex - 1)
    override fun compareTo(other: Path): Int = other.length.compareTo(this.length)

    fun nextSteps(grid: Grid<Char>): List<Coordinate> {
        return lastLoc.getSurroundingManhattan()
            .filter { it != beforeLastLoc }
            .filter { grid[it] != '#' }
    }
    fun stepTo(loc: Coordinate): Path {
        return Path(steps + loc)
    }

    fun isNotEmpty(): Boolean = steps.isNotEmpty()
    fun dropLast(): Path {
        return Path(steps.subList(0, steps.lastIndex))
    }

    fun pathFrom(lastLoc: Coordinate): Path {
        return Path(steps.subList(steps.indexOf(lastLoc), steps.size))
    }

    operator fun plus(other: Path): Path {
        return Path(steps + other.steps)
    }

}

data class Cheat(
    val removedWalls: List<Coordinate>,
    val stepToBlock: Coordinate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cheat

        return removedWalls == other.removedWalls
    }

    override fun hashCode(): Int {
        return removedWalls.hashCode()
    }

    fun getStep(coordinate: Coordinate): Int? {
        return removedWalls.withIndex().find { it.value == coordinate }?.index
    }

}

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

fun Grid<Char>.applyCheat(cheat: Cheat): Grid<Char> {
    return Grid(grid.mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            val removedWall = cheat.getStep(Coordinate(x, y))
            if(removedWall != null) removedWall.toChar()
            else if(cheat.stepToBlock == Coordinate(x, y)) '#'
            else c
        }
    })

}

data class CheatingResult(val cheat: Cheat, val grid: Grid<Char>, val quickestPath: Path, private val lengthToBeat: Int) {
    val gain = lengthToBeat - quickestPath.length
    override fun toString(): String {
        return cheat.toString() + quickestPath.length
    }
}

fun toCheatingResult(cheat: Cheat, grid: Grid<Char>, quickestPath: Path, lengthToBeat: Int): CheatingResult? {
    return if (quickestPath != null) CheatingResult(cheat, grid, quickestPath, lengthToBeat)
    else null
}