package com.github.shmvanhouten.adventofcode2023.day11

import com.github.shmvanhouten.adventofcode.utility.collectors.splitIntoTwo
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.pairs.mapFirst

fun Grid<Boolean>.expand(): Grid<Boolean> {
    return this.insertRowsIf { it.none { it } }.insertColumsif { it.none { it } }
}

fun Grid<Boolean>.countPathsBetweenAllGalaxies(emptySpaceLength: Long = 2): Long {
    val emptyLines = this.listEmptyColumnsAndRows()
    val galaxies = coordinatesMatching { it }
    return generateSequence(galaxies) { it }
        .mapIndexed { index, coordinates -> coordinates.subList(index, coordinates.size) }
        .takeWhile { it.size > 1 }
        .map { it.splitIntoTwo(1).mapFirst {it.first()} }
        .sumOf { (location, others) ->
            others.sumOf { otherLocation ->
                location.distanceFrom(otherLocation) + emptyLines.countEmptyLinesBetween(location, otherLocation) * (emptySpaceLength - 1)
            }
        }
}

fun Pair<List<Int>, List<Int>>.countEmptyLinesBetween(one: Coordinate, other: Coordinate): Int {
    val expandedColumns = (minOf(one.x, other.x)).until(maxOf(one.x, other.x)).count { this.first.contains(it) }
    val expandedRows = (minOf(one.y, other.y)).until(maxOf(one.y, other.y)).count { this.second.contains(it) }
    return expandedColumns + expandedRows
}

private fun Grid<Boolean>.listEmptyColumnsAndRows(): Pair<List<Int>, List<Int>> {
    val emptyYs = this.rows()
        .withIndex()
        .filter { it.value.none { it } }
        .map { it.index }
    val emptyXs = this.columns()
        .withIndex()
        .filter { it.value.none { it } }
        .map { it.index }
    return emptyXs to emptyYs
}
