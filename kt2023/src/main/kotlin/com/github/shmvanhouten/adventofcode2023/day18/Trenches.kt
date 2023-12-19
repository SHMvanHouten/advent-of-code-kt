package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGridFromCoordinates
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween
import com.github.shmvanhouten.adventofcode.utility.strings.words
import kotlin.math.abs

fun digHex(hexInstructions: String): Long {
    val trench = hexInstructions.lines()
        .map { toInstructionHex(it) }
    return dig(trench)
}

fun dig(instructions: String): Long = dig(instructions.lines().map { toInstruction(it) })

fun dig(instructions: List<Instruction>): Long {
    val trenches = getTrench(instructions)

    return shoeLace(trenches) + (trenches.circumference() / 2) + 1
}

private fun shoeLace(trenches: List<Coordinate>): Long =
    abs(trenches.windowed(2)
        .sumOf { (first, second) ->
            first.x.toLong() * second.y.toLong() - first.y.toLong() * second.x.toLong()
        }) / 2

private fun List<Coordinate>.circumference(): Long =
    windowed(2).sumOf { (first, second) ->
        (first..second).count().toLong() - 1
    }

private fun toInstruction(line: String): Instruction {
    val (turn, steps) = line.words()
    val direction = when(turn) {
        "R" -> EAST
        "L" -> WEST
        "U" -> NORTH
        "D" -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, steps.toInt())
}

private fun toInstructionHex(line: String): Instruction {
    val ins = line.substringBetween("(#", ")")
    val direction = when(ins.last()) {
        '0' -> EAST
        '2' -> WEST
        '3' -> NORTH
        '1' -> SOUTH
        else -> error("unknown $line")
    }
    return Instruction(direction, ins.substring(0, ins.lastIndex).toInt(16))
}

private fun getTrench(instructions: List<Instruction>): List<Coordinate> =
    instructions.runningFold(Coordinate(0, 0)) { loc: Coordinate, instruction: Instruction ->
        loc.move(instruction.direction, instruction.distance)
    }

data class Instruction(val direction: Direction, val distance: Int)

fun toGrid(input: String): Grid<Char> {
    val trench = getTrench(input.lines().map { toInstruction(it) })
        .windowed(2).flatMap { (c1, c2) -> c1..c2 }
    return charGridFromCoordinates(trench)
}
