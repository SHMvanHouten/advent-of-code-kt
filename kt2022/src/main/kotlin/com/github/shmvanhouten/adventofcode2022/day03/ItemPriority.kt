package com.github.shmvanhouten.adventofcode2022.day03

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun prioritySumOf(input: String): Int {
    return input.lines()
        .sumOf { findSharedItem(it).priority() }
}

fun prioritySumOfSharedItems(input: String): Int {
    return input.lines().windowed(3, 3)
        .sumOf { findSharedItem(it).priority() }
}

private fun findSharedItem(rucksack: String): Char {
    val (firstCompartment, secondCompartment) = rucksack.splitIntoTwo(rucksack.length / 2)
    return firstCompartment.first { secondCompartment.contains(it) }
}

fun findSharedItem(rucksacks: List<String>): Char {
    val (r1, r2, r3) = rucksacks
    val sharedBetweenAll3 = r1
        .filter { r2.contains(it) }.toSet()
        .filter { r3.contains(it) }
    assert(sharedBetweenAll3.size == 1)
    return sharedBetweenAll3.first()
}

private fun Char.priority(): Int {
    return if (this.isLowerCase()) this.code - 'a'.code + 1
    else this.code - 'A'.code + 27
}