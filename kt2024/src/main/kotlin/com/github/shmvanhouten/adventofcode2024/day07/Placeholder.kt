package com.github.shmvanhouten.adventofcode2024.day07

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun main() {
    readFile("/input-day07.txt")
        .lines()
        .onEach(::println)
}

fun part1(input: String) = input.lines()
    .map { it.parse() }
    .filter { isAValidLine(it) }
    .sumOf { it.first }

fun isAValidLine(line: String) = isAValidLine(line.parse())

fun isAValidLine(line: Pair<Long, List<Long>>): Boolean {
    val (result, values) = line
    return values.canGetToResult(result)

}

fun String.parse(): Pair<Long, List<Long>> {
    val (result, rest) = split(':')
    return result.toLong() to rest.trim().words().map { it.toLong() }
}

fun List<Long>.canGetToResult(goal: Long): Boolean {
    return permute().any { it == goal}
}

fun List<Long>.permute(): List<Long> {
    if(first() < 0) return emptyList()
    if(size < 2) error("cannot permute list of less than 2")
    if(size == 2) {
        val (first, second) = this
        return listOf(first + second, first * second)
    }
    val (first, second, rest) = this.extractFirstTwo()
    return (listOf((first + second)) + rest).permute() +
        (listOf((first * second)) + rest).permute()
}

fun <T>List<T>.extractFirstTwo(): Triple<T, T, List<T>> {
    val remaining = if(size == 2) {
        emptyList()
    } else {
        subList(2, size)
    }
    return Triple(first(), this[1], remaining)
}

fun <T>List<T>.head(): Pair<T, List<T>> {
    return this.first() to this.tail()
}

fun <T>List<T>.tail(): List<T> = subList(1, size)
