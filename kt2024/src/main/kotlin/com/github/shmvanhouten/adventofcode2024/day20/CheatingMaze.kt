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
            toCheatingResult(cheat, grid, quickestPath(grid, start, goal, quickestPath.length), quickestPath.length)
        }
}

fun possibleCheatsAlongPath(grid: Grid<Char>, quickestPath: Path): List<Cheat> {
    val steps = quickestPath.steps
    return steps.subList(0, steps.lastIndex).asSequence()
        .flatMap { step -> adjacentInnerWalls(step, grid).map { step to it } }
        .distinct()
        .map { (step, wall) -> Cheat(wall to wall, steps[steps.indexOf(step) + 1]) }
//        .map { (step, wall) -> (step to wall) to innerTiles(wall, grid).filter { it !in steps } }
//        .flatMap { (stepToNextStep, nextCandidates) ->
//            val (step, nextStep) = stepToNextStep
//            nextCandidates
//                .map { listOf(nextStep, it).sortedWith { a, b -> compareCoordinates(a, b) } }
//                .map { (a, b) -> Cheat(a to b, steps[steps.indexOf(step) + 1]) }
//        }
        .distinct().toList()
}

fun innerTiles(loc: Coordinate, grid: Grid<Char>): List<Coordinate> {
    return loc.getSurroundingManhattan()
        .filter { !grid.isOnPerimiter(it) }
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
    maxLength: Int = Int.MAX_VALUE
): Path? {
    val unfinishedPaths = priorityQueueOf(Path(listOf(start)))
    while (unfinishedPaths.isNotEmpty()) {
        val path = unfinishedPaths.poll()
        if(path.length >= maxLength) return null
        if (path.lastLoc == goal) return path
        unfinishedPaths.addAll(path.nextSteps(grid)
            .map { path.stepTo(it) })
    }
    return null
}

data class Path(
    val steps: List<Coordinate>
): Comparable<Path> {
    val length = steps.size - 1
    val lastLoc = steps.last()
    val beforeLastLoc = steps.getOrNull(steps.lastIndex - 1)
    override fun compareTo(other: Path): Int = other.length.compareTo(this.length)

    fun nextSteps(grid: Grid<Char>): List<Coordinate> {
        return lastLoc.getSurroundingManhattan()
            .filter { it != beforeLastLoc }
            .filter { grid[it] != '#' }
    }
    fun stepTo(loc: Coordinate): Path {
        return Path(steps + loc)
    }

}

private fun compareCoordinates(
    a: Coordinate,
    b: Coordinate
): Int {
    val compareTo = a.x.compareTo(b.x)
    return if (compareTo != 0) compareTo
    else a.y.compareTo(b.y)
}

data class Cheat(
    val removedWalls: Pair<Coordinate, Coordinate>,
    val stepToBlock: Coordinate
) {
    fun appliesTo(coordinate: Coordinate): Boolean =
        removedWalls.first == coordinate || removedWalls.second == coordinate

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cheat

        return removedWalls == other.removedWalls
    }

    override fun hashCode(): Int {
        return removedWalls.hashCode()
    }


}

fun priorityQueueOf(path: Path): PriorityQueue<Path> = PriorityQueue<Path>(listOf(path))

fun Grid<Char>.applyCheat(cheat: Cheat): Grid<Char> {
    return Grid(grid.mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            if(cheat.appliesTo(Coordinate(x, y))) '1'
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

fun toCheatingResult(cheat: Cheat, grid: Grid<Char>, quickestPath: Path?, lengthToBeat: Int): CheatingResult? {
    return if (quickestPath != null) CheatingResult(cheat, grid, quickestPath, lengthToBeat)
    else null
}