package com.github.shmvanhouten.adventofcode2022.day16

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun parse(input: String): Map<String, Valve> {
    return input.lines()
        .map { toValve(it) }
        .associateBy { it.name }
}

fun toValve(line: String): Valve {
    val words = line.words()
    val name = words[1]
    val flowRate = line.substringAfter("rate=").substringBefore(";").toInt()
    val connectedTo = words
        .filterIndexed { index, _ -> index >= 9 }
        .map { it.trim(',') }
        .toSet()
    return Valve(name, flowRate, connectedTo)
}
