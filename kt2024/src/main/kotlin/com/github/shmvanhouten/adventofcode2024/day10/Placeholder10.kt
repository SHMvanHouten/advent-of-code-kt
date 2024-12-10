package com.github.shmvanhouten.adventofcode2024.day10

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.CoordinateIndexedValue
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.gridTo

fun findTrailHeadScores(input: String): Long {
    val grid = gridTo(input) { it.digitToInt() }
    val trailheads = grid.withIndex().filter { it.item == 0 }.map { it.location }
    return trailheads.sumOf { calculateScore(it, grid) }
}

fun calculateScore(trailHead: Coordinate, grid: Grid<Int>): Long {
    val visitedLocations = mutableSetOf(trailHead)
    val unvisited = ArrayDeque(grid.move(trailHead, 0))
    val peaks = mutableSetOf<Coordinate>()
    while (unvisited.isNotEmpty()) {
        val (coord, value) = unvisited.removeFirst()
        visitedLocations += coord
        grid.move(coord, value).filter { !visitedLocations.contains(it.location) }
            .forEach {
                if(!visitedLocations.contains(it.location)) {
                    unvisited += it
                }
                if(it.item == 9) peaks += it.location
            }
    }
    return peaks.size.toLong()
}

fun Grid<Int>.move(coordinate: Coordinate, value: Int): List<CoordinateIndexedValue<Int, Coordinate>> {
    return Direction.entries.map { coordinate.move(it) }
        .filter { this.contains(it) && (this[it] - value) == 1  }
        .map { CoordinateIndexedValue(it, this[it]) }
}
