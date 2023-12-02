package com.github.shmvanhouten.adventofcode.utility.collectors

fun <E> Iterable<E>.productOf(transform: (E) -> Number): Long =
    fold(1L) { acc, number -> acc * transform(number).toLong() }

fun Iterable<Number>.product(): Long =
    fold(1L) {acc, number -> acc * number.toLong()}

@Deprecated(message = "use product instead", replaceWith = ReplaceWith("List<Number>.product"))
fun Iterable<Number>.multiply(): Long = product()