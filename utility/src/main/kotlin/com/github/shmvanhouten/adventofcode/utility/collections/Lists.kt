package com.github.shmvanhouten.adventofcode.utility.collections

fun <E> List<E>.allBefore(i: Int, predicate: (E) -> Boolean): Boolean =
    this.subList(0, i).all(predicate)

fun <E> List<E>.allAfter(i: Int, predicate: (E) -> Boolean) =
    this.subList(i + 1, this.size).all(predicate)

fun <T> List<T>.combineAll(): List<Pair<T, T>> {
    return this.flatMapIndexed { index, item ->
        this.subList(index + 1, this.size).map { item to it }
    }
}

fun <T, R> List<T>.combineAllWith(transform: (T, T) -> R): List<R> {
    return this.flatMapIndexed { index, item ->
        this.subList(index + 1, this.size).map { transform(item, it) }
    }
}