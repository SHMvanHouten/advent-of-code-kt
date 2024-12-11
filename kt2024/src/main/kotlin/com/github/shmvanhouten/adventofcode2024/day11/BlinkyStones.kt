package com.github.shmvanhouten.adventofcode2024.day11

import com.github.shmvanhouten.adventofcode.utility.strings.words

// each number will lead to a list of numbers after 25
// each of those numbers will also lead to a list of numbers after 25
// we could memoize those processes

// there are very few unique stones every time,
// so we can just count each stone, and do the next step * count (in the end I chose this...)

fun blink(input: String, nrOfBlinks: Int): Long {
    return generateSequence(
        parse(input).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    ) { stonesToCounts ->
        stonesToCounts.entries.flatMap { (stone, count) ->
            applyRules(stone).groupingBy { it }.eachCount()
                .mapValues { it.value * count }.entries
        }.groupingBy { it.key }.aggregate { _, acc, stoneToCount, _ ->
            (acc ?: 0L) + stoneToCount.value
        }
    }.drop(nrOfBlinks).first().values.sum()
}

private fun applyRules(
    stone: Stone
): List<Stone> = when {
    stone == 0L -> listOf(1)
    hasEvenNumberOfDigits(stone) -> stone.split()
    else -> listOf(stone * 2024)
}

fun Stone.split(): List<Stone> {
    val toString = this.toString()
    return listOf(
        toString.substring(0, toString.length/2).toLong(),
        toString.substring(toString.length/2, toString.length).toLong()
        )
}

fun hasEvenNumberOfDigits(stone: Stone): Boolean {
    return isEven(stone.toString().length)
}

fun isEven(n: Int): Boolean = n % 2 == 0

private fun parse(input: String): List<Stone> =
    input.words().map { it.toLong() }

typealias Stone = Long
