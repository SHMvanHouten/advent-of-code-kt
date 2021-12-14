package com.github.shmvanhouten.adventofcode2021.day14

import kotlin.math.max


fun countMostCommonAndLeastCommonElementAfterSynthesysRuns(
    template: String,
    rules: Map<String, Pair<String, String>>,
    amountOfRuns: Int
): Pair<Long, Long> {
    val pairCounts = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    return generateSequence(pairCounts) { pairs -> applyRules(pairs, rules)}
        .drop(amountOfRuns)
        .first()
        .let { countMostAndLeastCommonElement(it) }
}

private fun applyRules(pairs: Map<String, Long>, rules: Map<String, Pair<String, String>>):Map<String, Long> {
    val newPairs = pairs.map { (pair, count) -> rules[pair]!! to count }
        .flatMap { (twoPairs, count) -> listOf(twoPairs.first to count, twoPairs.second to count) }

    val newPairCounts = mutableMapOf<String, Long>()
    newPairs.forEach { (pair, count) -> newPairCounts.merge(pair, count, Long::plus) }
    return newPairCounts
}

fun countMostAndLeastCommonElement(pairCounts: Map<String, Long>) : Pair<Long, Long> {
    val elementCounts = countElements(pairCounts)
    val values = elementCounts.values
    return values.minOrNull()!! to values.maxOrNull()!!
}

private fun countElements(pairCounts: Map<String, Long>): Map<Char, Long> {
    val (startingElements, endingElements) = pairCounts
        .map { (pair, count) -> Pair(pair[0] to count, pair[1] to count) }
        .fracture()

    val startingElementCounts = toMapWithMerge(startingElements).entries.sortedBy { it.key }
    val endingElementCounts = toMapWithMerge(endingElements).entries.sortedBy { it.key }


    return startingElementCounts.zip(endingElementCounts)
        .map { (startingEntry, endingEntry) -> startingEntry.key to max(startingEntry.value, endingEntry.value) }
        .toMap()
}

private fun toMapWithMerge(startingElements: List<Pair<Char, Long>>): Map<Char, Long> {
    val startingElementCounts = mutableMapOf<Char, Long>()
    startingElements.forEach { (element, count) -> startingElementCounts.merge(element, count, Long::plus) }
    return startingElementCounts
}

private fun <T, Z> List<Pair<T, Z>>.fracture(): Pair<List<T>, List<Z>> {
    return this.map { it.first } to this.map { it.second }
}

fun applyRules(template: String, rules: Map<String, String>, steps: Int = 1): String {
    return generateSequence(template) { polymer -> polymer.windowed(size = 2).map { rules[it] }.joinToString("") + polymer.last()}
        .drop(steps)
        .first()
}

fun countMostCommonAndLeastCommonElement(polymer: String): Pair<Int, Int> {
    val elementCounts = polymer.toSet().map { uniqueChar -> polymer.count { it == uniqueChar } }
    return elementCounts.minOrNull()!! to elementCounts.maxOrNull()!!
}

