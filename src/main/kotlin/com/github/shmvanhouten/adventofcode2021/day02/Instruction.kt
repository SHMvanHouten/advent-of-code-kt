package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Direction

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
    return when(line) {
        "forward" -> Direction.EAST
        "down" -> Direction.SOUTH
        "up" -> Direction.NORTH

        else -> error("unkown direction $line")

    }
}

private fun String.words(): List<String> {
    return this.split(' ')
}
