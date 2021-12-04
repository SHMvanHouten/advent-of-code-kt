package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2021.depth.coordinate.Direction.*
import com.github.shmvanhouten.adventofcode2021.depth.coordinate.move

interface Submarine {
    fun navigate(instructions: List<Instruction>): Submarine {
        return instructions.fold(this) { submarine, instruction ->
            submarine.move(instruction)
        }
    }

    val location: Coordinate
    fun move(instruction: Instruction): Submarine
}

data class SimpleSubmarine(override val location: Coordinate = Coordinate(0, 0)) : Submarine {
    override fun move(instruction: Instruction): Submarine {
        return SimpleSubmarine(location = location.move(instruction.direction, instruction.steps))
    }
}

data class AimingSubmarine(override val location: Coordinate = Coordinate(0, 0), val aim: Int = 0) : Submarine {
    override fun move(instruction: Instruction): Submarine {
        return when (instruction.direction) {
            UP -> this.copy(aim = aimUp(aim, instruction.steps))
            DOWN -> this.copy(aim = aimDown(aim, instruction.steps))
            FORWARD -> this.copy(location = relocate(instruction))
        }
    }

    private fun relocate(
        instruction: Instruction,
    ) = location
        .move(instruction.direction, instruction.steps)
        .move(DOWN, instruction.steps * aim)

    private fun aimDown(
        aim: Int,
        amount: Int
    ) = aim + amount

    private fun aimUp(
        aim: Int,
        amount: Int
    ) = aim - amount
}