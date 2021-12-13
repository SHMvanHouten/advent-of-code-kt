package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.day13.FoldingLine.Y

fun fold(coordinates: Set<Coordinate>, foldingInstruction: FoldingInstruction): Set<Coordinate> {

    if(foldingInstruction.foldingLine == Y) {
        return coordinates.filter { it.y >= foldingInstruction.n }
            .map { it.copy(y = foldingInstruction.n - (it.y - foldingInstruction.n)) }.toSet() + coordinates.filter { it.y < foldingInstruction.n }
    } else {
        return coordinates.filter { it.x >= foldingInstruction.n }
            .map { it.copy(x = foldingInstruction.n - (it.x - foldingInstruction.n)) }.toSet() + coordinates.filter { it.x < foldingInstruction.n }
    }
}