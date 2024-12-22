package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import java.util.*
import kotlin.math.abs

private const val WALL = '#'

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

fun findCheats2(input: String, nrOfCheatsAllowed: Int = 2, mustBeFasterThan: Int = 100): Int {
    val grid = charGrid(input)
    val start = grid.firstLocationOf { it == 'S' }!!
    val goal = grid.firstLocationOf { it == 'E' }!!

    val quickestPath = quickestPath(grid, Path(listOf(start)), goal)!!
    val maxSize = quickestPath.length - mustBeFasterThan
    return countQuickerPathsWithCheats(grid, start, goal, nrOfCheatsAllowed, maxSize, quickestPath)
}

fun countQuickerPathsWithCheats(
    grid: Grid<Char>,
    start: Coordinate,
    goal: Coordinate,
    nrOfCheatsAllowed: Int,
    maxSize: Int,
    quickestPath: Path
): Int {
    val quickestPathSet = quickestPath.steps.toSet()
    val unfinishedPaths = mutableSetOf(CheatingPath(listOf(start)))
    val successfulPaths = mutableSetOf<Cheat2>()
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.first()
        unfinishedPaths -= path

        if (path.size + path.lastLoc.distanceFrom(goal) > maxSize) continue // drop path
        if(path.hasNotStartedCheatYet()) {
            unfinishedPaths += path.startingCheat()
        }  else if(path.isOutOfCheat(nrOfCheatsAllowed) && grid[path.lastLoc] == WALL ) {
            continue
        } else if(path.canStopCheating(grid)) {
            unfinishedPaths += path.stopCheating()
        }

        if(path.isOutOfCheat(nrOfCheatsAllowed)) {
            continue
        }

        if(path.isNotCheatingAnyMore() && path.lastLoc in quickestPathSet) {
            if((quickestPath.steps.lastIndex - quickestPath.steps.indexOf(path.lastLoc)) + path.size < maxSize ) {
                successfulPaths.add(Cheat2(path.cheat.first(), path.cheat.last()))
                continue
            }
        }

        if (path.lastLoc == goal) {
//            println(path.print(grid))
            successfulPaths.add(Cheat2(path.cheat.first(), path.cheat.last()))
//            if(successfulPaths.size >= 5) println("H213!@#$#@${successfulPaths.size}")
            continue
        }

        unfinishedPaths.addAll(
            path.nextSteps(grid).map { path.stepTo(it) }
        )
    }
    return successfulPaths.size
}

data class CheatingPath(val steps: List<Coordinate>, val cheat: List<Coordinate> = emptyList(), val isCheating: Boolean = false): Comparable<CheatingPath> {

    val size: Int = steps.size
    val lastLoc = steps.lastOrNull()?:Coordinate(-1,-1)
    private val beforeLastLoc = steps.getOrNull(steps.lastIndex - 1)

    fun hasNotStartedCheatYet(): Boolean = cheat.isEmpty()
    fun startingCheat(): CheatingPath = copy(cheat = listOf(steps.last()), isCheating = true)
    fun isOutOfCheat(nrOfCheatsAllowed: Int): Boolean = nrOfCheatsAllowed + 1 == cheat.size
    fun nextSteps(grid: Grid<Char>): List<Coordinate> {
        return lastLoc.getSurroundingManhattan()
            .filter { it !in steps }
            .filter { grid.contains(it) }
            .filter { isCheating || grid[it] != WALL }
            .filter { !justStartedCheating() || grid[it] == WALL }
    }

    private fun justStartedCheating(): Boolean = cheat.size == 1 && isCheating

    fun stepTo(loc: Coordinate): CheatingPath {
        return if(isCheating) {
            copy(steps = steps + loc, cheat = cheat + loc)
        } else {
            copy(steps = steps + loc)
        }
    }

    fun canStopCheating(grid: Grid<Char>): Boolean = isCheating && grid[lastLoc] != WALL && cheat.size > 1
    fun stopCheating(): CheatingPath = copy(cheat = cheat.subList(0, cheat.lastIndex), isCheating = false)

    override fun compareTo(other: CheatingPath): Int = this.size.compareTo(other.size)

    fun print(grid: Grid<Char>): String {
        val mutableGrid = grid.toMutableGrid()
        steps.subList(1, steps.lastIndex).forEach { mutableGrid[it] = '0' }
        cheat.forEachIndexed { i, c -> mutableGrid[c] = cheatChars[i] }
        return mutableGrid.toString()
    }

    fun isNotCheatingAnyMore(): Boolean = !isCheating && cheat.isNotEmpty()


}

private val cheatChars = "0123456789abcdefghijklmnop"

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

data class Cheat2(val start: Coordinate, val end: Coordinate)

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