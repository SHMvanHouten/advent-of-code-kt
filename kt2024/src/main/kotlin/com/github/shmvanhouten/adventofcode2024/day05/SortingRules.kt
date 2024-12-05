package com.github.shmvanhouten.adventofcode2024.day05

import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun part1(input: String): Int {
    val (rules, updates) = parse(input)
    return updates.filter { isCorrectlyOrdered(it, rules) }
        .sumOf { it[it.size/2] }
        .also { println(it) }
}

fun part2(input: String): Int {
    val (rules, updates) = parse(input)
    return updates.filter {!isCorrectlyOrdered(it, rules) }
        .map { reorder(it, rules) }
        .sumOf { it[it.size/2] }
}

private fun reorder(update: List<Int>, rules: Rules): List<Int> {
    return update.sortedWith{l, r ->
        if(comesBefore(l, r, rules)) -1
        else 1
    }
}

fun isCorrectlyOrdered(update: List<Int>, rules: Rules): Boolean {
    update.subList(0, update.lastIndex)
        .forEachIndexed { index, i ->
            if(update.subList(index + 1, update.size).any { comesBefore(it, i, rules) }) {
                return false
            }
    }
    return true
}

fun comesBefore(one: Int, other: Int, rules: Map<Int, List<Int>>): Boolean {
    return rules[one]?.contains(other)?: false
            || !(rules[other]?.contains(one)?: false)
}

fun parse(input: String): Pair<Rules, Updates> {
    val (rawRules, rawUpdates) = input.blocks()
    return rawRules.lines().map { it.split('|').map { it.toInt() } }.map { (f, s) -> f to s }.groupBy ({ it.first }, {it.second}) to
            rawUpdates.lines().map { it.split(',').map { it.toInt() } }
}

typealias Rules = Map<Int, List<Int>>
typealias Updates = List<List<Int>>
