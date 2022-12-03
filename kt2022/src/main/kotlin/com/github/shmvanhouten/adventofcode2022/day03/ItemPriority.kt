package com.github.shmvanhouten.adventofcode2022.day03

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun prioritySumOf(input: String): Int {
    return input.lines()
        .sumOf { findSharedItem(it).priority() }
}

fun prioritySumOfSharedItems(input: String): Int {
    return input.lines()
        .chunked(3)
        .sumOf { findSharedItem(it).priority() }
}

private fun findSharedItem(rucksack: String): Char {
    val (firstCompartment, secondCompartment) = rucksack.splitIntoTwo(rucksack.length / 2)
    return firstCompartment.first { secondCompartment.contains(it) }
}

fun findSharedItem(rucksacks: List<String>): Char {
    val sharedBetweenAll = rucksacks.reduce { s1, s2 ->
        s1.filter { s2.contains(it) }
    }.toSet()
    assert(sharedBetweenAll.size == 1)
    return sharedBetweenAll.first()
}

private fun Char.priority(): Int {
    return if (this.isLowerCase()) this.code - 'a'.code + 1
    else this.code - 'A'.code + 27
}