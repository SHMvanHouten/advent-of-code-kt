package com.github.shmvanhouten.adventofcode2021.day02

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun steer(instructions: List<Instruction>): Coordinate {
    return instructions.fold(Coordinate(0, 0)) { coordinate, instruction ->
        coordinate.move(instruction.direction, instruction.steps)
    }
}

fun steer2(instructions: List<Instruction>): Coordinate {

}