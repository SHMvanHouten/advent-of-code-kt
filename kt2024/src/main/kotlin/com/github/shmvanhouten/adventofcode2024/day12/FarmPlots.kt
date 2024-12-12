package com.github.shmvanhouten.adventofcode2024.day12

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.CoordinateProgression
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

fun calculatePlots(grid:Grid<Char>): List<Plot> {
    val plots = mutableMapOf<Char, List<Plot>>()
    grid.forEachIndexed { loc, char ->
        val newPlot = buildNewPlot(char, loc, grid)
        val plotsForChar = plots[char]
        plots[char] = if(plotsForChar == null) {
            listOf(newPlot)
        } else {
            val (adjacentPlots, nonAdjacent) = plotsForChar.partition { it.locations.any(loc.getSurroundingManhattan()::contains) }
            nonAdjacent + (adjacentPlots + newPlot).reduce(Plot::merge)
        }
    }
    return plots.values.flatten()
}

private fun buildNewPlot(id: Char, loc: Coordinate, grid: Grid<Char>): Plot {
    val perimeter = Direction.entries
        .map { loc.move(it) to it }
        .filter { (newLoc, _) -> (grid.getOrNull(newLoc) ?: '.') != id }
    return Plot(id, listOf(loc), perimeter)
}

fun List<Plot>.prices() = map { it.price() }
fun List<Plot>.discountPrices() = map { it.discountPrice() }

data class Plot(
    val id: Char,
    val locations: List<Coordinate>,
    val perimeter: List<Pair<Coordinate, Direction>>
) {
    private val area: Long
        get() = locations.size.toLong()

    fun price() = area * perimeter.size
    fun discountPrice(): Long = countSides() * area

    fun merge(other: Plot): Plot {
        return copy(
            locations = locations + other.locations,
            perimeter = this.perimeter + other.perimeter
        )
    }

    private fun countSides(): Int {
        val sides = mutableListOf<Pair<CoordinateProgression, Direction>>()
        perimeter.forEach { (loc, dir) ->
            val sideToAttachTo = sides.find { (prog, directionOfSide) -> directionOfSide == dir && prog.nextInLine(dir) == loc }
            if(sideToAttachTo == null) {
                sides += loc..loc to dir
            } else {
                sides -= sideToAttachTo
                sides += sideToAttachTo.first.append(loc) to dir
            }
        }

        return sides.size
    }

    private fun CoordinateProgression.nextInLine(directionFromPlotTile: Direction): Coordinate =
        this.end.getNeighbour(directionToMoveForNext(directionFromPlotTile))

    private fun directionToMoveForNext(sideDir: Direction): Direction = when(sideDir) {
        // Since we move from topLeft to bottomRight we can make these assumptions
        Direction.NORTH, Direction.SOUTH -> Direction.EAST
        Direction.EAST, Direction.WEST -> Direction.SOUTH
    }

}

private fun CoordinateProgression.append(loc: Coordinate): CoordinateProgression = this.start..loc
