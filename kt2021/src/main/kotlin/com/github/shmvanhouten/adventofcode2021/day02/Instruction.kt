package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2021.depth.coordinate.Direction

data class Instruction(val direction: Direction, val steps: Int)

fun parseInstructions(input: String): List<Instruction> {
    return input.lines().map { toInstruction(it) }
}

fun toInstruction(line: String): Instruction {
    return Instruction(
        toDirection(line.words()[0]),
        line.words()[1].toInt()
    )
}

fun toDirection(line: String): Direction {
    return Direction.valueOf(line.uppercase())
}

private fun String.words(): List<String> {
    return this.split(' ')
}
