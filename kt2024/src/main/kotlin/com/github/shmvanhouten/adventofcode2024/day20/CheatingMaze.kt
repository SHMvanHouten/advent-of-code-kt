package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import java.util.*
import kotlin.math.abs

fun findCheats(input: String, cheatLength: Int = 2): Sequence<CheatingResult> {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    val quickestPath = quickestPath(grid, Path(listOf(start)), goal)!!
    return possibleCheatsAlongPath(grid, quickestPath, cheatLength)
        .asSequence()
        .mapNotNull { cheat ->
            val pathFromCheat = quickestPath(grid, Path(cheat.pathWithCheat), goal, quickestPath)
            if (pathFromCheat != null) {
                CheatingResult(cheat, pathFromCheat, quickestPath.length)
            } else null
        }.filter { it.gain > 0 }
}

// starting position of the cheat is the point on the path we start from
fun possibleCheatsAlongPath(grid: Grid<Char>, quickestPath: Path, cheatLength: Int): List<Cheat> {
    val steps = quickestPath.steps
    return steps.subList(0, steps.lastIndex).withIndex().asSequence()
        .map { (index, step) -> step to quickestPath.steps.subList(0, index + 1) }
        .flatMap { (step, pathSoFar) -> grid.listAllOpenSpotsWithIn(step, cheatLength, pathSoFar.toSet())
            .map { pathSoFar + step to it }}
        .map { (pathSoFar, warp) -> Cheat(pathSoFar.dropLast(2) + directPath(pathSoFar.last(), warp), steps[pathSoFar.lastIndex]) }
        .toList()
}

fun directPath(starting: Coordinate, warp: Coordinate): List<Coordinate> {
    val (startX, endX) = listOf(starting.x, warp.x).sorted()
    val (startY, endY) = listOf(starting.y, warp.y).sorted()
    val walkDown = (startY..endY).map { y -> Coordinate(startX, y) }
    val cheatWalk = walkDown + (startX..endX).drop(1).map { x -> Coordinate(x, endY) }
    return if(cheatWalk.first() == starting) cheatWalk
    else cheatWalk.reversed()
}

fun quickestPath(input: String): Path {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    return quickestPath(grid, Path(listOf(start)), goal)!!
}

private fun quickestPath(
    grid: Grid<Char>,
    start: Path,
    goal: Coordinate,
    existingPath: Path = emptyPath()
): Path? {
    val unfinishedPaths = priorityQueueOf(start)
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.poll()
        val lastLoc = path.lastLoc
        if(lastLoc in existingPath) return path.dropLast() + existingPath.pathFrom(lastLoc)
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
    private val stepsAsSet: Set<Coordinate> by lazy { steps.toSet() }
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

    operator fun contains(lastLoc: Coordinate): Boolean = lastLoc in stepsAsSet

}

data class Cheat(
    val pathWithCheat: List<Coordinate>,
    val stepToBlock: Coordinate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cheat

        return pathWithCheat == other.pathWithCheat
    }

    override fun hashCode(): Int {
        return pathWithCheat.hashCode()
    }

}

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

//fun Grid<Char>.applyCheat(cheat: Cheat): Grid<Char> {
//    return Grid(grid.mapIndexed { y, row ->
//        row.mapIndexed { x, c ->
//            val removedWall = cheat.getStep(Coordinate(x, y))
//            if(removedWall != null) removedWall.toChar()
//            else if(cheat.stepToBlock == Coordinate(x, y)) '#'
//            else c
//        }
//    })
//
//}

data class CheatingResult(val cheat: Cheat, val quickestPath: Path, private val lengthToBeat: Int) {
    val gain = lengthToBeat - quickestPath.length
    override fun toString(): String {
        return cheat.toString() + quickestPath.length
    }
    fun print(grid: Grid<Char>): String {
        val mutableGrid = grid.toMutableGrid()
        quickestPath.steps.forEach { mutableGrid[it] = '0' }
        cheat.pathWithCheat.forEachIndexed { i, c -> mutableGrid[c] = i.toChar() }
        return mutableGrid.toString()
    }
}

fun <T> List<T>.sublistUntil(element: T): List<T> {
    return subList(0, indexOf(element))
}

fun Grid<Char>.listAllOpenSpotsWithIn(start: Coordinate, length: Int, mustNotEndIn: Set<Coordinate>): Set<Coordinate> {
    val startY = maxOf(start.y - length, 0)
    val endY = minOf(start.y + length, this.height - 1)
    return (startY..endY).flatMap { y ->
        val availableSteps = length - abs(start.y - y)
        val startX = maxOf(start.x - availableSteps, 0)
        val endX = minOf(start.x + availableSteps, this.width - 1)
        (startX..endX).map{x -> Coordinate(x, y)}
            .filter { this[it] != '#' }
            .filter { it !in mustNotEndIn }
    }.toSet()
}