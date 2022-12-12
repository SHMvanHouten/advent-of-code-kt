package com.github.shmvanhouten.adventofcode.utility.pairs

fun <T, R> nullablePair(first: T?, second: R?): Pair<T, R>? {
    return if (first == null || second == null) null
    else first to second
}