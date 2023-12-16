package com.github.shmvanhouten.adventofcode.utility.pairs

fun <T, R> nullablePair(first: T?, second: R?): Pair<T, R>? {
    return if (first == null || second == null) null
    else first to second
}

fun <A, B, R> Pair<A, B>.mapFirst(transform: (A) -> R): Pair<R, B> = transform(first) to second
fun <A, B, R> Pair<A, B>.mapSecond(transform: (B) -> R): Pair<A, R> = first to transform(second)