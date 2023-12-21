package com.github.shmvanhouten.adventofcode2023.day21

import com.github.shmvanhouten.adventofcode.utility.grid.Grid


fun Grid<Char>.takeSteps(targetSteps: Int): Int {
    var positions = setOf(this.firstCoordinateMatching { it == 'S' }!!)
    repeat(targetSteps) {
        positions = positions.flatMap {
            it.getSurroundingManhattan()
        }.filter { this.contains(it) }.filter { this[it] != '#'}.toSet()
    }
    return positions.size
}

