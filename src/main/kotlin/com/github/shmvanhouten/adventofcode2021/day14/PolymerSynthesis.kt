package com.github.shmvanhouten.adventofcode2021.day14

fun applyRules(template: String, rules: Map<String, String>, steps: Int = 1): String {
    return generateSequence(template) { polymer -> polymer.windowed(size = 2).map { rules[it] }.joinToString("") + polymer.last()}
        .drop(steps)
        .first()
}

fun countMostCommonAndLeastCommonElement(polymer: String): Pair<Int, Int> {
    val elementCounts = polymer.toSet().map { uniqueChar -> polymer.count { it == uniqueChar } }
    return elementCounts.minOrNull()!! to elementCounts.maxOrNull()!!
}

