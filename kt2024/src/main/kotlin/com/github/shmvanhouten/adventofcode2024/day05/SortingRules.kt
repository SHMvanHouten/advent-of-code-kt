package com.github.shmvanhouten.adventofcode2024.day05

import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun sumCorrectlyOrderedLists(input: String): Int {
    val (rules, updates) = parse(input)
    return updates.filter { isCorrectlyOrdered(it, rules) }
        .sumOf { it[it.size/2] }
}

fun reorderIncorrectlyOrderedLists(input: String): Int {
    val (rules, updates) = parse(input)
    return updates.filter {!isCorrectlyOrdered(it, rules) }
        .map { it.sortedWith { l, r -> rules.compare(l, r) } }
        .sumOf { it[it.size/2] }
}

private fun isCorrectlyOrdered(update: List<Int>, rules: Rules): Boolean =
    update.subList(0, update.lastIndex)
        .withIndex()
        .none { (index, i) ->
            update.subList(index + 1, update.size)
                .any { rules.comesBefore(it, i) }
        }

private fun Rules.comesBefore(one: Int, other: Int): Boolean =
    this[one]?.contains(other)?: false
        || !(this[other]?.contains(one)?: false)

private fun Rules.compare(l: Int, r: Int) =
    if (this.comesBefore(l, r)) -1
    else 1

private fun parse(input: String): Pair<Rules, Updates> {
    val (rawRules, rawUpdates) = input.blocks()
    return Pair(
        rawRules.lines()
            .map { it.split('|').map(String::toInt) }
            .map { (f, s) -> f to s }
            .groupBy({ it.first }, { it.second }),
        rawUpdates.lines().map { it -> it.split(',').map(String::toInt) })
}

typealias Rules = Map<Int, List<Int>>
typealias Updates = List<List<Int>>
