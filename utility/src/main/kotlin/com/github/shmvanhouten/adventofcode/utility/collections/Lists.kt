package com.github.shmvanhouten.adventofcode.utility.collections

fun <E> List<E>.allBefore(i: Int, predicate: (E) -> Boolean): Boolean =
    this.subList(0, i).all(predicate)

fun <E> List<E>.allAfter(i: Int, predicate: (E) -> Boolean) =
    this.subList(i + 1, this.size).all(predicate)