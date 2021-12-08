package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo

fun count1478digits(input: String): Int {
    val signals = input.lines().map { toSignal(it) }
    return signals.map { it.output }
        .sumOf { output -> output.count { signal -> isA1478(signal) } }
}

fun isA1478(signal: String): Boolean {
    val length = signal.length
    return length == 2 || length == 3 || length == 4 || length == 7
}

fun toSignal(line: String): Signal {
    val (input, output) = line.splitIntoTwo(" | ")
    return Signal(input.split(' '), output.split(' '))
}

data class Signal(val input: List<String>, val output: List<String>)