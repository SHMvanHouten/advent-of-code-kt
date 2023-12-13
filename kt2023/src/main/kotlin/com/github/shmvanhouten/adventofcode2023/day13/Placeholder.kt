package com.github.shmvanhouten.adventofcode2023.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    readFile("/input-day13.txt")
        .lines()
        .onEach(::println)
}

fun getReflectionValue(input: String): Int {
    val grid = charGrid(input)
    val row = grid.findContiguousIdenticalRowsIndices()
        .firstOrNull { grid.canHorizontallyExpandMirrorFromIndex(it) }?.plus(1)?.times(100) ?: 0
    if(row == 0) {
        return grid.findContiguousIdenticalColumnsIndices()
            .firstOrNull { grid.canVerticallyExpandMirrorFromIndex(it) }?.plus(1) ?: 0
    } else return row
}

private fun Grid<Char>.canVerticallyExpandMirrorFromIndex(i: Int): Boolean {
    var otherIndexDistance = 3
    (i - 1).downTo(0).forEach { index ->
        val otherIndex = index + otherIndexDistance
        if(otherIndex >= this.width) return true
        if(this.getColumn(index) != this.getColumn(otherIndex)) return false
        otherIndexDistance += 2
    }
    return true
}

private fun Grid<Char>.canHorizontallyExpandMirrorFromIndex(i: Int): Boolean {
    var otherIndexDistance = 3
    (i - 1).downTo(0).forEach { index ->
        val otherIndex = index + otherIndexDistance
        if(otherIndex >= this.height) return true
        if(this[index] != this[otherIndex]) return false
        otherIndexDistance += 2
    }
    return true
}

private fun Grid<Char>.findContiguousIdenticalColumnsIndices(): List<Int> {
    return this.columns().windowed(2)
        .mapIndexed { index, (c1, c2) -> (c1 == c2) to index}
        .filter { it.first }
        .map { it.second }
}

private fun Grid<Char>.findContiguousIdenticalRowsIndices(): List<Int> {
    return this.rows().windowed(2)
        .mapIndexed { index, (c1, c2) -> (c1 == c2) to index}
        .filter { it.first }
        .map { it.second }
}
