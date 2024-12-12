package com.github.shmvanhouten.adventofcode2024.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

fun main() {
    readFile("/input-day12.txt")
        .lines()
        .onEach(::println)
}

fun calculatePlots(grid:Grid<Char>): List<Plot> {
    val plots = mutableMapOf<Char, List<Plot>>()
    grid.forEachIndexed { loc, char ->
        val surrounding = Direction.entries
            .map { loc.move(it) }
        val perimiter = surrounding
            .count { (grid.getOrNull(it) ?: '?') != char }.toLong()
        val plotsForChar = plots[char]
        val newPlot = Plot(
            id = char,
            locations = listOf(loc),
            area = 1,
            perimiter = perimiter
        )
        plots[char] = if(plotsForChar != null) {
            val (adjacentPlots, nonAdjacent) = plotsForChar.partition { it.locations.any(surrounding::contains) }
            nonAdjacent + (adjacentPlots + newPlot).reduce(Plot::merge)
        } else {
            listOf(newPlot)
        }
    }
    return plots.values.flatten()
}

fun List<Plot>.prices() = map { it.price() }

data class Plot(
    val id: Char,
    val locations: List<Coordinate>,
    val area: Long,
    val perimiter: Long
) {
    fun price() = area * perimiter
    fun merge(other: Plot): Plot {
        return copy(
            locations = locations + other.locations,
            area = this.area + other.area,
            perimiter = this.perimiter + other.perimiter
        )
    }
}
