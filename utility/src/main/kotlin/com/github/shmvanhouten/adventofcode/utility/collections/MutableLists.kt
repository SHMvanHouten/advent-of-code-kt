package com.github.shmvanhouten.adventofcode.utility.collections

fun <T> MutableList<T>.removeAfterIndex(index: Int) {
    for (i in this.lastIndex.downTo(index)) {
        this.removeAt(i)
    }
}