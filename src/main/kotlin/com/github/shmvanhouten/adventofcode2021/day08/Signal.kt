package com.github.shmvanhouten.adventofcode2021.day08

data class Signal(val input: List<String>, val output: List<String>)

fun toSignal(line: String): Signal {
    val (input, output) = line.split(" | ")
    return Signal(input.split(' '), output.split(' '))
}