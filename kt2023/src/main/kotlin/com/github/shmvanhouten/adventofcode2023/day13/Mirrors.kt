package com.github.shmvanhouten.adventofcode2023.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid


fun getReflectionValue(input: String): Int {
    val grid = charGrid(input)
    return getReflectionValue(grid)
}

fun getCleanReflectionValue(input: String): Int {
    val grid = charGrid(input)
    val oldCleanReflectionValue = getReflectionValue(grid)
    return grid.listWithEachPixelFlipped()
        .map { getReflectionValue(it, oldCleanReflectionValue) }
        .first { it != 0 }
}

private fun getReflectionValue(grid: Grid<Char>, _dontMatchOn: Int = -1): Int {
    var dontMatchOnRow = -1
    var dontMatchOnColumn = -1
    if(_dontMatchOn >= 100) {
        dontMatchOnRow = (_dontMatchOn / 100) - 1
    } else {
        dontMatchOnColumn = (_dontMatchOn) - 1
    }
    val row = grid.findContiguousIdenticalRowsIndices(dontMatchOnRow)
        .firstOrNull { grid.canHorizontallyExpandMirrorFromIndex(it) }?.plus(1)?.times(100) ?: 0
    if (row == 0) {
        return grid.findContiguousIdenticalColumnsIndices(dontMatchOnColumn)
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

private fun Grid<Char>.findContiguousIdenticalColumnsIndices(dontMatchOn: Int): List<Int> {
    return this.columns().windowed(2)
        .mapIndexed { index, (c1, c2) -> (c1 == c2) to index}
        .filter { it.first }
        .filter { it.second != dontMatchOn }
        .map { it.second }
}

private fun Grid<Char>.findContiguousIdenticalRowsIndices(dontMatchOn: Int): List<Int> {
    return this.rows().windowed(2)
        .mapIndexed { index, (c1, c2) -> (c1 == c2) to index}
        .filter { it.first }
        .filter { it.second != dontMatchOn }
        .map { it.second }
}


private fun Grid<Char>.listWithEachPixelFlipped(): Sequence<Grid<Char>> {
    return this.rows().asSequence().flatMapIndexed { y, row ->
        row.asSequence().mapIndexed { x: Int, pixel: Char ->
            if(pixel == '.') {
                this.withPixelAt('#', Coordinate(x, y))
            } else {
                this.withPixelAt('.', Coordinate(x, y))
            }
        }
    }
}

private fun Grid<Char>.withPixelAt(pixel: Char, loc: Coordinate): Grid<Char> {
    return Grid(this.grid.mapIndexed { y, row ->
        row.mapIndexed { x, c -> if(loc == Coordinate(x, y)) pixel else c }
    })
}
