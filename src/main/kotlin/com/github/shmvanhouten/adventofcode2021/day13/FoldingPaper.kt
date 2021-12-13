package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

fun fold(coordinates: Set<Coordinate>, foldingInstruction: FoldingInstruction): Set<Coordinate> {
        val (toFold, toRemain) = splitCoordinatesToFold(coordinates, foldingInstruction)
        return toRemain.toSet() + toFold.map { foldingInstruction.applyFold(it) }
}

private fun splitCoordinatesToFold(
    coordinates: Set<Coordinate>,
    foldingInstruction: FoldingInstruction
): Pair<List<Coordinate>, List<Coordinate>> {
    val groupedByFilter = coordinates.groupBy { foldingInstruction.applyFilter(it) }
    return groupedByFilter[true]!! to groupedByFilter[false]!!
}