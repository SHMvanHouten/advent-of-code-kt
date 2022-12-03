package com.github.shmvanhouten.adventofcode.utility.coordinate

fun <E: Comparable<E>> List<E>.extremes(): Pair<E, E>? {
    if (this.isEmpty()) return null
    else return this.minOrNull()!! to this.maxOrNull()!!
}