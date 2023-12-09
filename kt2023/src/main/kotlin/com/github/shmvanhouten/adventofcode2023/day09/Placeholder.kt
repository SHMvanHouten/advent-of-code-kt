package com.github.shmvanhouten.adventofcode2023.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.words

fun main() {
    readFile("/input-day09.txt")
        .lines()
        .map { toDataSet(it) }
        .map { extrapolate(it) }
        .onEach(::println)
}

fun extrapolate(line: List<Long>): Long {
    val lineHistory = mutableListOf(line)
    var nextLine = line.zipWithNext().map { it.second - it.first }
    while (nextLine.any { it != 0L }) {
        lineHistory += nextLine
        nextLine = nextLine.zipWithNext().map { it.second - it.first }
    }
    return lineHistory.reversed().fold(0L) {acc, longs ->  acc + longs.last()}
}


fun extrapolateBack(line: List<Long>): Long {
    val lineHistory = mutableListOf(line)
    var nextLine = line.zipWithNext().map { it.second - it.first }
    while (nextLine.any { it != 0L }) {
        lineHistory += nextLine
        nextLine = nextLine.zipWithNext().map { it.second - it.first }
    }
    return lineHistory.reversed().fold(0L) {acc, longs ->
        longs.first() - acc
    }
}

fun toDataSet(it: String) = it.words().map { it.toLong() }

