package com.github.shmvanhouten.adventofcode2024.day01

import kotlin.math.abs

fun findIdDistances(input: String): Int {
    val (left, right) = toLeftAndRightList(input)
    return left.sorted().zip(right.sorted()).sumOf { abs(it.first - it.second) }
}

fun sumSimilarityScores(input: String): Int {
    val (left, right) = toLeftAndRightList(input)
    val res = right.groupingBy { it }.eachCount()
    return left.sumOf { l -> (res[l] ?: 0) * l }
}

private fun toLeftAndRightList(input: String) = input
    .lines()
    .map {
        it
            .split(' ')
            .filter(String::isNotBlank)
            .mapNotNull(String::toIntOrNull)
    }
    .map { it[0] to it[1] }
    .unzip()
