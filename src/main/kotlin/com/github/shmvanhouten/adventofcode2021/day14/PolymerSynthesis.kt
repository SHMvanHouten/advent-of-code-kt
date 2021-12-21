package com.github.shmvanhouten.adventofcode2021.day14

import com.github.shmvanhouten.adventofcode.utility.collectors.toMap
import kotlin.math.max

fun countElementsAfterSynthesis(
    template: String,
    rules: InsertionRules,
    amountOfRuns: Int
): Map<Char, Long> {
    return generateSequence(toPairCounts(template), rules::applyTo)
        .drop(amountOfRuns)
        .first()
        .let { countElements(it) }
}

fun countMostAndLeastCommonElement(counts: Collection<Long>): Pair<Long, Long> {
    return counts.minOrNull()!! to counts.maxOrNull()!!
}

private fun toPairCounts(template: String) =
    template.windowed(2)
        .groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }

private fun countElements(pairCounts: Map<String, Long>): Map<Char, Long> {
    val (startingElements, endingElements) = pairCounts
        .map { (pair, count) -> Pair(pair[0] to count, pair[1] to count) }
        .unzip()

    val startingElementCounts = startingElements.toMap(Long::plus).entries.sortedBy { it.key }
    val endingElementCounts = endingElements.toMap(Long::plus).entries.sortedBy { it.key }

    if (startingElementCounts.map { it.key } != endingElementCounts.map { it.key })
        error("fails if starting or ending element of the polymer is unique, " +
                    "you will have to deal with that with that input!")

    return startingElementCounts.zip(endingElementCounts)
        .associate { (startingEntry, endingEntry) ->
            startingEntry.key to max(startingEntry.value, endingEntry.value)
        }
}

