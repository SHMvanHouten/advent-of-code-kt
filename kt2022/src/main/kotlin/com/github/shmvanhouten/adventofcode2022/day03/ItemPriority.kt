package com.github.shmvanhouten.adventofcode2022.day03

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun prioritySumOf(input: String): Int {
    return input.lines()
        .sumOf { findCommonItemPriorityOf(it) }
}

private fun findCommonItemPriorityOf(rucksack: String): Int {
    val (firstCompartment, secondCompartment) = rucksack.splitIntoTwo(rucksack.length / 2)
    return firstCompartment.first { secondCompartment.contains(it) }.priority()
}

private fun Char.priority(): Int {
    return if (this.isLowerCase()) this.code - 'a'.code + 1
    else this.code - 'A'.code + 27
}