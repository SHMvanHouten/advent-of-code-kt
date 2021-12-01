package com.github.shmvanhouten.adventofcode2021.day01

fun countNumberOfIncreases(input: List<Long>):Int {
    return input.windowed(2).count { it[1] > it[0] }
}

fun countNumberOfIncreasesFor3SlidingWindow(input: List<Long>): Int {
    return input.windowed(3)
        .map { it.sum() }
        .windowed(2)
        .count { it[1] > it[0] }
}