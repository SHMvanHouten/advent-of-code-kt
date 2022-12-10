package com.github.shmvanhouten.adventofcode2022.day10

import com.github.shmvanhouten.adventofcode2022.day09.words
import kotlin.math.abs

fun runInstructions(instructions: String): List<Long> {
    return runInstructions(instructions.lines())
}

fun runInstructions(instructions: List<String>): List<Long> {
    val registerXAtCycle = mutableListOf<Long>(1)
    for (instruction in instructions) {
        registerXAtCycle.add(registerXAtCycle.last())
        if(instruction != "noop") {
            registerXAtCycle.add(registerXAtCycle.last() + instruction.words()[1].toInt())
        }
    }
    return registerXAtCycle
}

fun draw(instructions: List<String>): String {
    return runInstructions(instructions)
        .chunked(40).joinToString("\n") { chunk -> drawLine(chunk) }
}

fun drawLine(instructions: List<Long>): String {
    return instructions.mapIndexed { index, regx ->
        if(abs(regx - index) <= 1) '█'
        else ' '
    }.joinToString("")
}

fun List<Long>.registerXAtCycle(cycle: Int): Long {
    return this[cycle - 1] * cycle
}

fun sumSignalStrengths(result: List<Long>): Long {
    return (20..220).step(40).sumOf { result[it - 1] * it }
}


