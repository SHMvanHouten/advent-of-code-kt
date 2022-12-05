package com.github.shmvanhouten.adventofcode2022.day05

fun Stacks.perform(instructions: List<Instruction>): Stacks {
    for (instruction in instructions) {
        0.until(instruction.amount).forEach { _ ->
            val from = this[instruction.from]!!
            this[instruction.to]!!.addLast(from.last())
            from.removeLast()
        }
    }
    return this
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