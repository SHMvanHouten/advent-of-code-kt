package com.github.shmvanhouten.adventofcode2023.day11

import com.github.shmvanhouten.adventofcode.utility.grid.Grid

fun Grid<Boolean>.expand(): Grid<Boolean> {
    return this.insertRowsIf { it.none { it } }.insertColumsif { it.none { it } }
}

fun Grid<Boolean>.countPathsBetweenAllGalaxies(): Int {
    val galaxies = coordinatesMatching { it }
    return generateSequence(galaxies) { it }
        .mapIndexed { index, coordinates -> coordinates.subList(index, coordinates.size) }
        .takeWhile { it.size > 1 }
        .sumOf { coordinates ->
            val first = coordinates.first()
            coordinates.subList(1, coordinates.size)
            .sumOf { first.distanceFrom(it) }
        }
}