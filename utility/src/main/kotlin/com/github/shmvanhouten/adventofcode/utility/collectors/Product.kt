package com.github.shmvanhouten.adventofcode.utility.collectors

fun <E> List<E>.productOf(transform: (E) -> Number): Long =
    map(transform)
        .product()

fun List<Number>.product(): Long =
    fold(1L) {acc, number -> acc * number.toLong()}

@Deprecated(message = "use product instead", replaceWith = ReplaceWith("List<Number>.product"))
fun List<Number>.multiply(): Long = product()