package com.github.shmvanhouten.adventofcode.utility.collections

fun <T> arrayDequeOf(item: T): ArrayDeque<T> = ArrayDeque(setOf(item))