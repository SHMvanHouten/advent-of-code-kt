package com.github.shmvanhouten.adventofcode2024.day04

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.RelativePosition
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

private val example = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
""".trimIndent()

fun main() {
    val input = readFile("/input-day04.txt")
    val p2 = part2(input)
    println(p2)
    println(part2(example))
}

fun part2(input: String): Int {
    val grid = charGrid(input)
    val aposes = grid.withIndex()
        .filter { it.item == 'M' }
        .flatMap { countXMases(it.location, grid) }
    return aposes
        .count { pos -> aposes.count { it == pos } == 2 }/2
}

fun countXMases(location: Coordinate, grid: Grid<Char>): List<Coordinate> {
    val aposes = listOf(
        RelativePosition.NORTH_EAST,
        RelativePosition.NORTH_WEST,
        RelativePosition.SOUTH_EAST,
        RelativePosition.SOUTH_WEST,
    ).mapNotNull { grid.readsXmasInDirection(location, it) }
    return aposes
}

fun Grid<Char>.readsXmasInDirection(location: Coordinate, direction: RelativePosition): Coordinate? {
    val apos = location.move(direction, 1)
    val spos = location.move(direction, 2)
    return if(this[location] == 'M'
        && this.contains(apos) && this[apos] == 'A'
        && this.contains(spos) && this[spos] == 'S') {
        apos
    } else {
        null
    }
}

