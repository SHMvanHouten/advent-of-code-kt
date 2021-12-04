package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.Direction.*

fun steer(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Coordinate(0, 0)) { coordinate, instruction ->
        coordinate.move(instruction.direction, instruction.steps)
    }
}

fun steer2(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Submarine()) { submarine, instruction ->
        submarine.move(instruction)
    }.location
}

private fun aimDown(
    aim: Int,
    amount: Int
) = aim + amount

private fun aimUp(
    aim: Int,
    amount: Int
) = aim - amount

private data class Submarine(val location: Coordinate = Coordinate(0, 0), val aim: Int = 0) {
    fun move(instruction: Instruction): Submarine {
        return when (instruction.direction) {
            NORTH -> this.copy(aim = aimUp(aim, instruction.steps))
            SOUTH -> this.copy(aim = aimDown(aim, instruction.steps))
            EAST -> this.copy(location = relocate(instruction))
            else -> error("unknown direction ${instruction.direction}")
        }
    }

    private fun relocate(
        instruction: Instruction,
    ) = location
        .move(instruction.direction, instruction.steps)
        .move(SOUTH, instruction.steps * aim)
}