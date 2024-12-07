package com.github.shmvanhouten.adventofcode2024.day07

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun sumValidEquations(input: String) =
    sumValidEquations(input, listOf(Long::plus, Long::times))

fun sumValidEquationsIncludingConcat(input: String) =
    sumValidEquations(input, listOf(Long::plus, Long::times, ::concatenated))

private fun sumValidEquations(input: String, operations: List<(Long, Long) -> Long>) = input.lines()
    .map { it.parse() }
    .filter {isAValidLine(it, operations)}
    .sumOf { it.first }

fun isAValidLine(line: String) = isAValidLine(line.parse(), listOf(Long::plus, Long::times))

fun isAValidLine(line: Pair<Long, List<Long>>, operations: List<(Long, Long) -> Long>): Boolean {
    val (result, values) = line
    return values.canGetToResult(result, operations)

}

fun String.parse(): Pair<Long, List<Long>> {
    val (goal, rest) = split(':')
    return goal.toLong() to rest.trim().words().map { it.toLong() }
}

fun List<Long>.canGetToResult(goal: Long, operations: List<(Long, Long) -> Long>): Boolean {
    return permute(goal, operations).any { it == goal}
}

fun List<Long>.permute(goal: Long, operations: List<(Long, Long) -> Long>): List<Long> {
    if (first() > goal) return emptyList()
    if (size < 2) error("cannot permute list of less than 2")
    if (size == 2) return operations.map { it.invoke(this[0], this[1]) }

    val (first, second, rest) = this.extractFirstTwo()
    return operations
        .flatMap { (listOf(it.invoke(first, second)) + rest)
        .permute(goal, operations) }
}

fun concatenated(first: Long, second: Long): Long = (first.toString() + second.toString()).toLong()

fun <T>List<T>.extractFirstTwo(): Triple<T, T, List<T>> {
    val remaining = if(size == 2) {
        emptyList()
    } else {
        subList(2, size)
    }
    return Triple(first(), this[1], remaining)
}
