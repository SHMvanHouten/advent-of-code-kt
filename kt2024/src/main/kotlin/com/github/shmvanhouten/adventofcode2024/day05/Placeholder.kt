package com.github.shmvanhouten.adventofcode2024.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun main() {
    part1(example)
    part1(readFile("/input-day05.txt"))


}

private fun part1(input: String) {
    val (rules, updates) = parse(input)
    updates.filter { isCorrectlyOrdered(it, rules) }
        .sumOf { it[it.size/2] }
        .also { println(it) }
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

private val example = """
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13

    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
""".trimIndent()