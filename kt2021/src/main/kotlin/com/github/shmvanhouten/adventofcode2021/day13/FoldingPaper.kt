package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate

fun fold(coordinates: Set<Coordinate>, foldingInstruction: FoldingInstruction): Set<Coordinate> {
    val (toFold, toRemain) = coordinates.partition { foldingInstruction.applyFilter(it) }
    return toRemain.toSet() + toFold.map { foldingInstruction.applyFold(it) }
}
