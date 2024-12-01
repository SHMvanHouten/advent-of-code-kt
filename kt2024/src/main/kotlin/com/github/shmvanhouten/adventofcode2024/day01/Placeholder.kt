package com.github.shmvanhouten.adventofcode2024.day01

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import kotlin.math.abs

fun main() {

    val input = readFile("/input-day01.txt")
    part1(input)
    part2(input)
}

private fun part1(input: String) {
    val (left, right) = toLeftAndRightList(input)
    println(left.sorted().zip(right.sorted()).sumOf { abs(it.first - it.second) })
}

fun part2(input: String) {
    val (left, right) = toLeftAndRightList(input)
    println(left.sumOf { l -> right.count { r -> l == r } * l })
}

private fun toLeftAndRightList(input: String) = input
    .lines()
    .map { it.split(' ').filter(String::isNotBlank) }
    .map { it.mapNotNull(String::toIntOrNull) }
    .map { it[0] to it[1] }
    .unzip()

