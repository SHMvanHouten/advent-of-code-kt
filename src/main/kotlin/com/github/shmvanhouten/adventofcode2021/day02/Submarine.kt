package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.Direction.*

interface Submarine {
    fun navigate(instructions: List<Instruction>): Submarine {
        return instructions.fold(this) { submarine, instruction ->
            submarine.move(instruction)
        }
    }
    val location: Coordinate
    fun move(instruction: Instruction): Submarine
}
data class SimpleSubmarine(override val location: Coordinate = Coordinate(0, 0)): Submarine {
    override fun move(instruction: Instruction): Submarine {
        return SimpleSubmarine(location = location.move(instruction.direction, instruction.steps))
    }
}

data class AimingSubmarine(override val location: Coordinate = Coordinate(0, 0), val aim: Int = 0): Submarine {
    override fun move(instruction: Instruction): Submarine {
        return when (instruction.direction) {
            NORTH -> this.copy(aim = aimUp(aim, instruction.steps))
            SOUTH -> this.copy(aim = aimDown(aim, instruction.steps))
            EAST -> this.copy(location = relocate(instruction))
            else -> error("unrecognized direction ${instruction.direction}")
        }
    }

    private fun relocate(
        instruction: Instruction,
    ) = location
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
}