package com.github.shmvanhouten.adventofcode2021.day14

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun parseSimple(input: String): Pair<String, Map<String, String>> {
    val (polymerTemplate, insertionRules) = input.blocks()
    return polymerTemplate to toInsertionResults(insertionRules)
}

private fun toInsertionResults(insertionRules: String): Map<String, String> {
    return insertionRules.lines()
        .map { it.split(" -> ") }
        .associate { (start, insertChar) -> start to start[0] + insertChar }
}

fun parse(input: String): Pair<String, InsertionRules> {
    val (polymerTemplate, insertionRules) = input.blocks()
    return polymerTemplate to toInsertionRules(insertionRules)
}

fun toInsertionRules(insertionRules: String): InsertionRules {
    return insertionRules.lines()
        .map { it.splitIntoTwo(" -> ") }
        .let { InsertionRules(it) }
}
