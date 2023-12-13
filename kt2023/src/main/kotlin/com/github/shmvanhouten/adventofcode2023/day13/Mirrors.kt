package com.github.shmvanhouten.adventofcode2023.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid


fun getReflectionValue(input: String): Int =
    getReflectionValue(charGrid(input))

fun getCleanReflectionValue(input: String): Int {
    val grid = charGrid(input)
    val lineExclusion = LineExclusion(getReflectionValue(grid))
    return grid.listWithEachPixelFlipped()
        .map { getReflectionValue(it, lineExclusion) }
        .first { it != 0 }
}

private fun getReflectionValue(grid: Grid<Char>, lineExclusion: LineExclusion = noExclusion()): Int {

    return grid.rows().findFirstIndexThatReflects(lineExclusion.forRows())?.plus(1)?.times(100)
        ?:grid.columns().findFirstIndexThatReflects(lineExclusion.forColumns())?.plus(1) ?: 0
}

fun noExclusion(): LineExclusion {
    return LineExclusion(-1)
}

private fun List<List<Char>>.findFirstIndexThatReflects(dontMatchOnRow: Int) =
    this.findContiguousIdenticalLines(dontMatchOnRow)
    .firstOrNull { canExpand(it, this) }

private fun List<List<Char>>.findContiguousIdenticalLines(dontMatchOn: Int): List<Int> =
    windowed(2)
        .mapIndexed { index, (c1, c2) -> (c1 == c2) to index }
        .filter { it.first }
        .filter { it.second != dontMatchOn }
        .map { it.second }

private fun canExpand(i: Int, rowsOrColumns: List<List<Char>>): Boolean {
    var otherIndexDistance = 3
    (i - 1).downTo(0).forEach { index ->
        val otherIndex = index + otherIndexDistance
        if(otherIndex >= rowsOrColumns.size) return true
        if(rowsOrColumns[index] != rowsOrColumns[otherIndex]) return false
        otherIndexDistance += 2
    }
    return true
}

private fun Grid<Char>.listWithEachPixelFlipped(): Sequence<Grid<Char>> {
    return this.rows().asSequence().flatMapIndexed { y, row ->
        row.asSequence().mapIndexed { x: Int, pixel: Char ->
            this.copyWithPixelAt(pixel.flip(), Coordinate(x, y))
        }
    }
}

class LineExclusion(private val reflectionValue: Int) {
    fun forRows(): Int =  if(reflectionValue >= 100) (reflectionValue/100) - 1
    else -1
    fun forColumns(): Int = if(reflectionValue< 100) reflectionValue - 1
    else -1
}

private fun Char.flip(): Char =
    if(this == '.') '#'
    else '.'

private fun Grid<Char>.copyWithPixelAt(pixel: Char, loc: Coordinate): Grid<Char> {
    return Grid(this.grid.mapIndexed { y, row ->
        row.mapIndexed { x, c -> if(loc == Coordinate(x, y)) pixel else c }
    })
}
