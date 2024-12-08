package com.github.shmvanhouten.adventofcode.utility.collections

import com.github.shmvanhouten.adventofcode.utility.chars.repeat

fun <T> Collection<T>.joinToEvenlySpaced(
    separator: Char = ' ',
    spaceSize: Int = 2,
    buffer: StringBuilder = StringBuilder()
): String {
    val lastIndex = this.size - 1
    for ((i, element) in this.withIndex()) {
        buffer.append(element)
        if(i < lastIndex) buffer.append(separator.repeat(spaceSize - element.toString().length))
    }
    return buffer.toString()
}

fun <T> Collection<T>.countDuplicates(minNrOfDuplications: Int = 2): Int =
    this.groupingBy { it }.eachCount()
        .count { (_, v) -> v >= minNrOfDuplications }

fun <T> Collection<T>.doesNotContain(e: T) = !this.contains(e)