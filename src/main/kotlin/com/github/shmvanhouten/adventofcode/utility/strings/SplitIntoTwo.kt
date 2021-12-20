package com.github.shmvanhouten.adventofcode.utility.strings

fun String.splitIntoTwo(delimiter: String): Pair<String, String> {
    val split = this.split(delimiter, limit = 2)
    return Pair(split[0], split[1])
}

fun String.splitIntoTwo(index: Int): Pair<String, String> {
    val firstPart = this.substring(0, index)
    val secondPart = this.substring(index, this.length)
    return Pair(firstPart, secondPart)
}