package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode2020.coordinate.Direction
import com.github.shmvanhouten.adventofcode2020.coordinate.Direction.*

fun steer(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Coordinate(0, 0)) { coordinate, instruction ->
        coordinate.move(instruction.direction, instruction.steps)
    }
}

fun steer2(instructions: List<Instruction>): Coordinate {
    var coordinate = Coordinate(0,0)
    var aim = 0
    for (instruction in instructions) {
        when(instruction.direction) {
            NORTH -> aim -= instruction.steps
            SOUTH -> aim += instruction.steps
            EAST -> {
                coordinate = coordinate
                    .move(instruction.direction, instruction.steps)
                    .move(SOUTH, instruction.steps * aim)

            }
            else -> error("unknown direction ${instruction.direction}")
        }
    }
    return coordinate
}