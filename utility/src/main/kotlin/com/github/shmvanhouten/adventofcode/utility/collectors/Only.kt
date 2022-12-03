package com.github.shmvanhouten.adventofcode.utility.collectors

fun <T> Collection<T>.only(): T {
    assert(this.size == 1)
    return this.first()
}