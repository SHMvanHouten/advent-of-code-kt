package com.github.shmvanhouten.adventofcode2023.day11

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid

fun Grid<Boolean>.expand(): Grid<Boolean> {
    return this.insertRowsIf { it.none { it } }.insertColumsif { it.none { it } }
}

fun Grid<Boolean>.countPathsBetweenAllGalaxies(expansion: Long = 1): Long {
    val (emptyXs, emptyYs) = this.listEmptyColumnsAndRows()
    val galaxies = coordinatesMatching { it }
    return generateSequence(galaxies) { it }
        .mapIndexed { index, coordinates -> coordinates.subList(index, coordinates.size) }
        .takeWhile { it.size > 1 }
        .sumOf { coordinates ->
            val first = coordinates.first()
            coordinates.subList(1, coordinates.size)
            .sumOf {
                val distance =
                    first.distanceFrom(it).toLong() +
                            (countEmptyColumnsAndRowsBetween(first, it, emptyXs, emptyYs) * expansion)
                distance
            }
        }
}

fun countEmptyColumnsAndRowsBetween(one: Coordinate, other: Coordinate, emptyXs: List<Int>, emptyYs: List<Int>): Int {
    val expandedColumns = (minOf(one.x, other.x)).until(maxOf(one.x, other.x)).count { emptyXs.contains(it) }
    val expandedRows = (minOf(one.y, other.y)).until(maxOf(one.y, other.y)).count { emptyYs.contains(it) }
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
