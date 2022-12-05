package com.github.shmvanhouten.adventofcode2022.day05

fun Stacks.execute(instructions: List<Instruction>): Stacks {
    for (instruction in instructions) {
        this.execute(instruction)
    }
    return this
}

private fun Stacks.execute(instruction: Instruction) {
    repeat(instruction.amount) {
        this[instruction.to]!!.addLast(
            this[instruction.from]!!.removeLast()
        )
    }
}

fun Stacks.executeV9001(instructions: List<Instruction>): Stacks {
    for (instruction in instructions) {
        executeV9001(instruction)
    }
    return this
}

private fun Stacks.executeV9001(instruction: Instruction) {
    this[instruction.to]!!.addAll(this[instruction.from]!!.takeLast(instruction.amount))
    repeat(instruction.amount) { this[instruction.from]!!.removeLast() }
}

fun Stacks.getTopCrates(): String {
    return this.entries.sortedBy { it.key }
        .map { it.value.last() }
        .joinToString("")
}

typealias Crate = Char

typealias Stacks = Map<Int, ArrayDeque<Crate>>

data class Instruction(
    val amount: Int,
    val from: Int,
    val to: Int
)