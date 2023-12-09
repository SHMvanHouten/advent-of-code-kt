package com.github.shmvanhouten.adventofcode2023.day09

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun extrapolate(line: List<Long>): Long =
    extrapolateUntil0(line).sumOf { it.last() }

fun extrapolateBack(line: List<Long>): Long =
    extrapolateUntil0(line).toList().reversed()
        .map { it.first() }
        .reduce { acc, x -> x - acc }

private fun extrapolateUntil0(line: List<Long>) =
    generateSequence(line) { nextLine -> nextLine.zipWithNext().map { it.second - it.first } }
        .takeWhile { thisLine -> thisLine.any { it != 0L } }

fun toDataSet(it: String) = it.words().map { it.toLong() }

