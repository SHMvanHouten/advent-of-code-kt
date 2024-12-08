package com.github.shmvanhouten.adventofcode2024.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collections.combineAll
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun main() {
    val input = readFile("/input-day08.txt")
    println(part1(input))
    println(part1(input).size)
}

fun part1(input: String): List<Coordinate> {
    val charGrid = charGrid(input)
    return charGrid.withIndex()
        .filter { it.item != '.' }
        .groupBy({ it.item }, { it.location })
        .flatMap { (a, locs) ->
            locs.combineAll()
                .flatMap { (a, b) -> listResultingAntinodes(a, b, charGrid) }
        }.distinct()
}

fun listResultingAntinodes(a: Coordinate, b: Coordinate, charGrid: Grid<Char>): List<Coordinate> {
    val dif = b - a
    return listOf(a - dif, b + dif).filter { charGrid.contains(it) }
}

private val example = """
    ............
    ........0...
    .....0......
    .......0....
    ....0.......
    ......A.....
    ............
    ............
    ........A...
    .........A..
    ............
    ............
""".trimIndent()