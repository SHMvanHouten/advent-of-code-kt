package com.github.shmvanhouten.adventofcode2023.day08

import com.github.shmvanhouten.adventofcode.utility.strings.words


fun toNetworkInstructions(instructions: String, network: String): NetworkInstructions {
    return NetworkInstructions(
        instructions.map { c ->
            when(c) {
                'L' -> {it -> it.left}
                'R' -> {it -> it.right}
                else -> throw NotImplementedError("")
            }
        },
        network.lines().map { toElement(it) }.toMap()
    )
}

fun toElement(line: String): Pair<String, Elements> {
    val (key, v1, v2) = line.filter { it.isLetterOrDigit() || it == ' ' }.words().filter { it.isNotBlank() }
    return key to Elements(v1, v2)
}

data class NetworkInstructions(val instructions: List<(Elements) -> String>, val network: Map<String, Elements>) {
    fun traverseUntil(target: String): List<String> {
        var current = "AAA"
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

