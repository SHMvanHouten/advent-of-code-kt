package com.github.shmvanhouten.adventofcode.utility.ranges

fun IntRange.contains(other: IntRange): Boolean {
    return this.first <= other.first
            && this.last >= other.last
}