package com.github.shmvanhouten.adventofcode2022.day04

import com.github.shmvanhouten.adventofcode.utility.ranges.contains

fun oneFullyContainsTheOther(one: IntRange, other: IntRange): Boolean {
    return one.contains(other) || other.contains(one)
}

fun theyOverlap(one: IntRange, other: IntRange): Boolean {
    return one.any { other.contains(it) }
}