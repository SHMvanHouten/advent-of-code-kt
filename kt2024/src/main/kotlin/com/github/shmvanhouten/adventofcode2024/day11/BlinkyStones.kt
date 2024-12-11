package com.github.shmvanhouten.adventofcode2024.day11

import com.github.shmvanhouten.adventofcode.utility.strings.words

// each number will lead to a list of numbers after 25
// each of those numbers will also lead to a list of numbers after 25
// we could memoize those processes

// there are very few unique stones every time,
// so we can just count each stone, and do the next step * count (in the end I chose this...)

fun blinkCounting(input: String, target: Int): Long {
    val stones = parse(input)
    var stonesToCount: Map<Stone, Long> = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(target) {
        val newStonesCount = mutableMapOf<Stone, Long>()
        stonesToCount.entries.forEach { (stone, count) ->
            applyRules(stone).groupingBy { it }.eachCount().mapValues { it.value * count }
                .forEach { newStonesCount.merge(it.key, it.value, Long::plus) }

        }
        stonesToCount = newStonesCount
    }
    return stonesToCount.values.sum()
}

fun blink(input: String, times: Int): List<Stone> = blink(parse(input), times)

fun blink(stones: List<Stone>, times: Int): List<Stone> {
    return generateSequence(stones) {
        blink(it)
    }
        .onEach { println(it) }
        .drop(times)
        .first()
}

private fun blink(stones: List<Stone>): List<Stone> {
    val newStones = mutableListOf<Stone>()
    stones.forEach {
        newStones.addAll(applyRules(it))
    }
    return newStones
}

private fun applyRules(
    stone: Stone
): List<Stone> {

    return when {
        stone == 0L -> listOf(1)
        hasEvenNumberOfDigits(stone) -> stone.split()
        else -> listOf(stone * 2024)
    }
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
