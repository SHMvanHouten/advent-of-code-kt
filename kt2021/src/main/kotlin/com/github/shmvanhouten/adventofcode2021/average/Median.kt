package com.github.shmvanhouten.adventofcode2021.average

fun List<Int>.median(): Int {
    val sorted = this.sorted()
    val i = sorted.size / 2
    return if (this.size % 2 == 0) {
        ((sorted[i] + sorted[i - 1]) / 2)
    } else {
        sorted[i]
    }
}

fun List<Long>.median(): Long {
    val sorted = this.sorted()
    val i = sorted.size / 2
    return if (this.size % 2 == 0) {
        ((sorted[i] + sorted[i - 1]) / 2)
    } else {
        sorted[i]
    }
}