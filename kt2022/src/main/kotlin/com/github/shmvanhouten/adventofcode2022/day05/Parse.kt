package com.github.shmvanhouten.adventofcode2022.day05

import com.github.shmvanhouten.adventofcode.utility.blocks

fun parse(input: String): Pair<Stacks, List<Instruction>> {
    val (rawCrates, rawInstructions) = input.blocks()
    return toCrates(rawCrates) to toInstructions(rawInstructions)
}

fun toCrates(rawCrates: String): Stacks {
    val reversedCrates = rawCrates.lines().reversed().tail()
    val stackLocations = reversedCrates.first().mapIndexedNotNull { i, c ->
        if (c.isLetter()) i
        else null
    }
    val crateStacks: Map<Int, ArrayDeque<Crate>> = reversedCrates.flatMap { cratesLine ->
        toLineOfCratesWithLineNumber(cratesLine, stackLocations)
    }
        .groupBy({it.first}, {it.second})
        .mapValues { ArrayDeque(it.value) }

    return crateStacks
}

private fun toLineOfCratesWithLineNumber(
    cratesLine: String,
    stackLocations: List<Int>
) = cratesLine.mapIndexedNotNull { i, maybeCrate ->
    if (maybeCrate.isLetter()) stackLocations.indexOf(i) + 1 to maybeCrate
    else null
}

fun <E> List<E>.tail(): List<E> {
    return if(size <= 1) emptyList()
    else subList(1, size)
}

fun toInstructions(rawInstructions: String): List<Instruction> {
    return rawInstructions.lines()
        .map { it.words() }
        .map { Instruction(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
}

private fun String.words(): List<String> {
    return split(' ')
}
