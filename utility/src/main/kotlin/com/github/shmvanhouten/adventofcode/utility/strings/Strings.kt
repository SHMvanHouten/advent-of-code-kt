package com.github.shmvanhouten.adventofcode.utility.strings

fun String.words(): List<String> {
    return split(' ')
}

fun String.blocks(): List<String> {
    return this.split("\n\n")
}

fun String.substringBetween(start: String, end: String): String {
    return substringAfter(start).substringBefore(end)
}