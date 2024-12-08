package com.github.shmvanhouten.adventofcode2024.day08

import com.github.shmvanhouten.adventofcode.utility.collections.combineAll
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun listAntinodes(input: String): List<Coordinate> {
    val charGrid = charGrid(input)
    return listAntinodes(charGrid, ::listResultingAntiNodes)
}

fun listAntinodesOnALine(input: String): List<Coordinate> {
    val charGrid = charGrid(input)
    return listAntinodes(charGrid, ::listResultingAntiNodesAnyDistance)
}

private fun listAntinodes(
    charGrid: Grid<Char>,
    listAntinodesResultingFrom: (Coordinate, Coordinate, Grid<Char>) -> List<Coordinate>
): List<Coordinate> {
    return charGrid.withIndex()
        .filter { it.item != '.' }
        .groupBy({ it.item }, { it.location })
        .flatMap { (_, locs) ->
            locs.combineAll()
                .flatMap { (a, b) -> listAntinodesResultingFrom(a, b, charGrid) }
        }.distinct()
}

fun listResultingAntiNodesAnyDistance(a: Coordinate, b: Coordinate, charGrid: Grid<Char>): List<Coordinate> {
    val dif = b - a
    val sequence1 = generateSequence(a) { it + dif }.takeWhile { charGrid.contains(it) }
    val sequence2 = generateSequence(a) { it - dif }.takeWhile { charGrid.contains(it) }
    return (sequence1 + sequence2).toList()
}

fun listResultingAntiNodes(a: Coordinate, b: Coordinate, charGrid: Grid<Char>): List<Coordinate> {
    val dif = b - a
    return listOf(a - dif, b + dif).filter { charGrid.contains(it) }
}
