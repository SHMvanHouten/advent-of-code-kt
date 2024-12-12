package com.github.shmvanhouten.adventofcode2024.day12

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

fun calculatePlots(grid:Grid<Char>): List<Plot> {
    val plots = mutableMapOf<Char, List<Plot>>()
    grid.forEachIndexed { loc, char ->
        val surrounding = Direction.entries
            .map { loc.move(it) to it }
        val perimeter = surrounding
            .filter { (newLoc, _) -> (grid.getOrNull(newLoc) ?: '?') != char }
        val plotsForChar = plots[char]
        val newPlot = Plot(
            id = char,
            locations = listOf(loc),
            area = 1,
            perimeter = perimeter
        )
        plots[char] = if(plotsForChar != null) {
            val (adjacentPlots, nonAdjacent) = plotsForChar.partition { it.locations.any(loc.getSurroundingManhattan()::contains) }
            nonAdjacent + (adjacentPlots + newPlot).reduce(Plot::merge)
        } else {
            listOf(newPlot)
        }
    }
    return plots.values.flatten()
}

fun List<Plot>.prices() = map { it.price() }
fun List<Plot>.discountPrices() = map { it.discountPrice() }

data class Plot(
    val id: Char,
    val locations: List<Coordinate>,
    val area: Long,
    val perimeter: List<Pair<Coordinate, Direction>>
) {
    fun price() = area * perimeter.count()
    fun merge(other: Plot): Plot {
        return copy(
            locations = locations + other.locations,
            area = this.area + other.area,
            perimeter = this.perimeter + other.perimeter
        )
    }

    fun discountPrice(): Long {
        val sides = countSides()
        return sides * area
    }

    private fun countSides(): Int {
        val sides = mutableListOf<Pair<CoordinateProgression, Direction>>()
        perimeter.forEach { (loc, dir) ->
            val sideToAttachTo = sides.find { (prog, sideDir) -> sideDir == dir && prog.nextInLine(dirFromSideDir(sideDir)) == loc }
            if(sideToAttachTo != null) {
                sides -= sideToAttachTo
                sides += sideToAttachTo.first.start..loc to dir
            } else {
                sides += loc..loc to dir
            }
        }

        return sides.size
    }

    private fun dirFromSideDir(sideDir: Direction): Direction = when(sideDir) {
        Direction.NORTH, Direction.SOUTH -> Direction.EAST
        Direction.EAST, Direction.WEST -> Direction.SOUTH
    }
}
