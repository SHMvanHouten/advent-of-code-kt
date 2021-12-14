package com.github.shmvanhouten.adventofcode2021.day14

import kotlin.math.max


fun countMostCommonAndLeastCommonElementAfterSynthesisRuns(
    template: String,
    rules: Map<String, Pair<String, String>>,
    amountOfRuns: Int
): Pair<Long, Long> {
    val pairCounts = toPairCounts(template)
    return generateSequence(pairCounts) { pairs -> applyRules(pairs, rules)}
        .drop(amountOfRuns)
        .first()
        .let { countElements(it) }
        .let { countMostAndLeastCommonElement(it.values) }
}

private fun toPairCounts(template: String) =
    template.windowed(2)
        .groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }

private fun applyRules(pairs: Map<String, Long>, rules: Map<String, Pair<String, String>>):Map<String, Long> {
    return pairs.map { (pair, count) -> rules[pair]!! to count }
        .flatMap { (twoPairs, count) -> listOf(twoPairs.first to count, twoPairs.second to count) }
        .toMap(Long::plus)
}

fun countMostAndLeastCommonElement(counts: Collection<Long>) : Pair<Long, Long> {
    return counts.minOrNull()!! to counts.maxOrNull()!!
}

private fun countElements(pairCounts: Map<String, Long>): Map<Char, Long> {
    val (startingElements, endingElements) = pairCounts
        .map { (pair, count) -> Pair(pair[0] to count, pair[1] to count) }
        .fracture()

    val startingElementCounts = startingElements.toMap(Long::plus).entries.sortedBy { it.key }
    val endingElementCounts = endingElements.toMap(Long::plus).entries.sortedBy { it.key }

    return startingElementCounts.zip(endingElementCounts)
        .associate { (startingEntry, endingEntry) -> startingEntry.key to max(startingEntry.value, endingEntry.value) }
}

private fun <F, S> List<Pair<F, S>>.toMap(mergeFunction: (S, S) -> S): Map<F, S> {
    val map = mutableMapOf<F, S>()
    this.forEach { (key: F, value: S) -> map.merge(key, value, mergeFunction) }
    return map.toMap()
}

private fun <T, Z> List<Pair<T, Z>>.fracture(): Pair<List<T>, List<Z>> {
    return this.map { it.first } to this.map { it.second }
}
