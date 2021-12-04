package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.Direction.*

fun steer(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Coordinate(0, 0)) { coordinate, instruction ->
        coordinate.move(instruction.direction, instruction.steps)
    }
}

fun steer2(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Coordinate(0,0) to 0) { (coordinate, aim), instruction ->
        performMove(instruction, coordinate, aim)
    }.first
}

private fun performMove(
    instruction: Instruction,
    coordinate: Coordinate,
    aim: Int
) = when (instruction.direction) {
    NORTH -> coordinate to aimUp(aim, instruction.steps)
    SOUTH -> coordinate to aimDown(aim, instruction.steps)
    EAST -> move(coordinate, instruction, aim) to aim
    else -> error("unknown direction ${instruction.direction}")
}

private fun move(
    coordinate: Coordinate,
    instruction: Instruction,
    aim: Int
) = coordinate
    .move(instruction.direction, instruction.steps)
    .move(SOUTH, instruction.steps * aim)

private fun aimDown(
    aim: Int,
    amount: Int
) = aim + amount

private fun aimUp(
    aim: Int,
    amount: Int
) = aim - amount