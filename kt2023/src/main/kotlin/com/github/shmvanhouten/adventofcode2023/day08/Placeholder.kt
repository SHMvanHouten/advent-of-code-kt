package com.github.shmvanhouten.adventofcode2023.day08

import com.github.shmvanhouten.adventofcode.utility.strings.words


fun toNetworkInstructions(instructions: String, network: String): NetworkInstructions1 {
    return NetworkInstructions1(
        instructions.map { c ->
            when(c) {
                'L' -> {it -> it.left}
                'R' -> {it -> it.right}
                else -> throw NotImplementedError("")
            }
        },
        network.lines().map { com.github.shmvanhouten.adventofcode2023.day08.part2.toElement(it) }.toMap()
    )
}

fun toElement(line: String): Pair<String, Elements> {
    val (key, v1, v2) = line.filter { it.isLetterOrDigit() || it == ' ' }.words().filter { it.isNotBlank() }
    return key to Elements(v1, v2)
}

data class NetworkInstructions1(val instructions: List<(Elements) -> String>, val network: Map<String, Elements>) {
    fun traverseUntil(target: String, start: String = "AAA"): List<String> {
        var current = start
        val traversed = mutableListOf<String>()
        while (true) {
            instructions.forEach { take ->
                current = take(network[current]!!)
                traversed += current
                if (current == target) return traversed
            }
        }
    }
}

data class Elements(val left: String, val right: String)

