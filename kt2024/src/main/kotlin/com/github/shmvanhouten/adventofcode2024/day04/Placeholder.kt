package com.github.shmvanhouten.adventofcode2024.day04

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    val input = readFile("/input-day04.txt")
    val p1 = part1(input)
    println(p1)
}

fun part1(input: String): Int {
    val grid = charGrid(input)
    return grid.withIndex()
        .filter { it.item == 'X' }
        .sumOf { countXMases(it.location, grid) }
}

fun countXMases(location: Coordinate, grid: Grid<Char>): Int {
    return RelativePosition.entries.count { grid.readsXmasInDirection(location, it) }
}

fun Grid<Char>.readsXmasInDirection(location: Coordinate, direction: RelativePosition): Boolean {
    val mpos = location.move(direction)
    val apos = location.move(direction, 2)
    val spos = location.move(direction, 3)
    return this[location] == 'X'
            && this.contains(mpos) && this[mpos] == 'M'
            && this.contains(apos) && this[apos] == 'A'
            && this.contains(spos) && this[spos] == 'S'
}

