package com.github.shmvanhouten.adventofcode2022.day01

import com.github.shmvanhouten.adventofcode.utility.blocks

fun carryingTheMost(input: String): Long {
    return calculateWhatElvesAreCarrying(input)
        .maxOrNull()?:error("no elf found")
}

private fun calculateWhatElvesAreCarrying(input: String): List<Long> {
    return input.blocks()
        .map { elf -> elf.lines().map { it.toLong() }.sum() }
}

fun top3ElvesTotal(input: String): Long {
    return calculateWhatElvesAreCarrying(input)
        .sortedDescending()
        .take(3).sum()
}
