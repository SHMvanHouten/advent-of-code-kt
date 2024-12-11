package com.github.shmvanhouten.adventofcode2024.day11

import com.github.shmvanhouten.adventofcode.utility.strings.words


fun blink(input: String, times: Int): List<Stone> = blink(parse(input), times)

fun blink(stones: List<Stone>, times: Int): List<Stone> {
    return generateSequence(stones) {
        blink(it)
    }.drop(times)
        .first()
}

private fun blink(stones: List<Stone>): List<Stone> {
    val newStones = mutableListOf<Stone>()
    stones.forEach {
        when {
            it == 0L -> newStones.add(1)
            hasEvenNumberOfDigits(it) -> newStones.addAll(it.split())
            else -> newStones.add(it * 2024)
        }
    }
    return newStones
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
