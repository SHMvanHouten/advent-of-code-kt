package com.github.shmvanhouten.adventofcode2024.day08

import com.github.shmvanhouten.adventofcode.utility.collections.combineAll
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid

fun listAntinodes(input: String): List<Coordinate> {
    val charGrid = charGrid(input)
    return listAntinodes(charGrid, Grid<Char>::listResultingAntiNodes)
}

fun listAntinodesOnALine(input: String): List<Coordinate> {
    val charGrid = charGrid(input)
    return listAntinodes(charGrid, Grid<Char>::listResultingAntiNodesAnyDistance)
}

private fun listAntinodes(
    charGrid: Grid<Char>,
    listAntinodesResultingFrom: Grid<Char>.(Coordinate, Coordinate) -> List<Coordinate>
): List<Coordinate> {
    return charGrid.withIndex()
        .filter { it.item != '.' }
        .groupBy({ it.item }, { it.location })
        .flatMap { (_, locs) ->
            locs.combineAll()
                .flatMap { (a, b) -> charGrid.listAntinodesResultingFrom(a, b) }
        }.distinct()
}

fun <T> Grid<T>.listResultingAntiNodesAnyDistance(a: Coordinate, b: Coordinate): List<Coordinate> {
    val dif = b - a
    return (
            stepInGrid(a, dif, Coordinate::plus)
              + stepInGrid(b, dif, Coordinate::minus)
        ).toList()
}

private fun <T> Grid<T>.stepInGrid(
    a: Coordinate,
    dif: Coordinate,
    operation: (Coordinate, Coordinate) -> Coordinate = Coordinate::plus
) = generateSequence(a) { operation(it, dif) }.takeWhile { contains(it) }

fun Grid<Char>.listResultingAntiNodes(a: Coordinate, b: Coordinate): List<Coordinate> {
    val dif = b - a
    return listOf(a - dif, b + dif).filter { contains(it) }
}
