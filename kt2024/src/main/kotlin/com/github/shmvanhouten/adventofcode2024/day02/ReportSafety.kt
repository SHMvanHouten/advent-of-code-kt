package com.github.shmvanhouten.adventofcode2024.day02

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun String.parse(): List<Int> {
    return words().map { it.toInt() }
}

fun List<Int>.isSafe(): Boolean {
    val diffs = zipWithNext { a, b -> a - b }
    return (diffs.all { it > 0 } || diffs.all { it < 0 })
            && diffs.all { it in -3..3 }
}

fun List<Int>.isSafeDampened(): Boolean {

    return List(size) { index ->
        val mutable = toMutableList()
        mutable.removeAt(index)
        mutable
    }.any { it.isSafe() }
}
