package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.blocks
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinate

fun parseFoldingInstructions(input: String): Pair<Set<Coordinate>, List<FoldingInstruction>> {
    val (coordinates, instructions) = input.blocks()
    return coordinates.lines().map { toCoordinate(it) }.toSet() to instructions.lines().map { toFoldingInstrucions(it) }
}

fun toFoldingInstrucions(line: String) : FoldingInstruction {
    val (_, _, instruction) = line.split(' ')
    val(foldingLine, n) = instruction.split('=')
    return FoldingInstruction(
        FoldingLine.valueOf(foldingLine.uppercase()),
        n.toInt()
    )
}
