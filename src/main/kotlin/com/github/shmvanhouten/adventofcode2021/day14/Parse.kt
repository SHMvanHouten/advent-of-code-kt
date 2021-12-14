package com.github.shmvanhouten.adventofcode2021.day14

import com.github.shmvanhouten.adventofcode.utility.blocks

fun parse(input: String): Pair<String, Map<String, String>> {
    val (polymerTemplate, insertionRules) = input.blocks()
    return polymerTemplate to toInsertionResults(insertionRules)
}

fun toInsertionResults(insertionRules: String): Map<String, String> {
    return insertionRules.lines()
        .map { it.split(" -> ") }
        .map { (start, insertChar) -> start to start[0] + insertChar }
        .toMap()
}
