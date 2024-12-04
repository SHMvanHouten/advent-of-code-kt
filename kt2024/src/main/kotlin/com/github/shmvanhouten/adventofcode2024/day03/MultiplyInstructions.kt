package com.github.shmvanhouten.adventofcode2024.day03

import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

// PART 1
fun calculateAllProducts(input: String): Long {
    return findProducts(input).map { it.second }
        .sum()
}

// PART 2
fun calculateProductsInDoRanges(
    input: String
): Long {
    val doRanges = findDoRanges(input)

    return findProducts(input).filter { (value, _) ->
        doRanges.any { input.indexOf(value) in it }
    }
        .map { it.second }
        .sum()
}

private fun findProducts(input: String): Sequence<Pair<String, Long>> {
    return Regex("""mul\((\d)*,(\d)*\)""")
        .findAll(input)
        .map { it.value }
        .map { it to it.calculateProduct() }
}

private fun findDoRanges(input: String): List<IntRange> {

    val ranges = mutableListOf<IntRange>()
    var lastCanDoIndex: Int? = 0
    sortDoAndDontRanges(input)
        .forEach { (index, canDo) ->
            if (lastCanDoIndex != null && !canDo) {
                ranges += (lastCanDoIndex!!..index)
                lastCanDoIndex = null

            } else if (lastCanDoIndex == null && canDo) {
                lastCanDoIndex = index
            }
        }

    if (lastCanDoIndex != null) {
        ranges += lastCanDoIndex!!..input.length
    }
    return ranges.toList()
}

private fun sortDoAndDontRanges(input: String) =
    (input.indicesOfAll("do()").map { it to true } + input.indicesOfAll("don't()")
        .map { it to false })
        .sortedBy { it.first }

private fun String.calculateProduct(): Long {
    return substringBetween("mul(", ")").split(',').map { it.toLong() }.product()
}

private fun String.indicesOfAll(element: String): List<Int> {
    return generateSequence(indexOf(element)) { indexOf(element, it + 1) }
        .takeWhile { it != -1 }.toList()
}
