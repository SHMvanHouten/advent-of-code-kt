package com.github.shmvanhouten.adventofcode2022.day04

import com.github.shmvanhouten.adventofcode.utility.ranges.contains

fun theyOverlap(one: IntRange, other: IntRange): Boolean {
    return one.contains(other) || other.contains(one)
}