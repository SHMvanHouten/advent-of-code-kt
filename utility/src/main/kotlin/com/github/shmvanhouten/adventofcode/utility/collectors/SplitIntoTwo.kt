package com.github.shmvanhouten.adventofcode.utility.collectors

fun <E> List<E>.splitIntoTwo(indexToSplitReversedList: Int): Pair<List<E>, List<E>> {
    val firstPart = this.subList(0, indexToSplitReversedList)
    val secondPart = this.subList(indexToSplitReversedList, this.size)
    return Pair(firstPart, secondPart)
}