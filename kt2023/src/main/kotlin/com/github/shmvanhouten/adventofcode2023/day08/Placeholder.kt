package com.github.shmvanhouten.adventofcode2023.day08

import com.github.shmvanhouten.adventofcode.utility.compositenumber.leastCommonMultiple
import com.github.shmvanhouten.adventofcode.utility.strings.words
import java.math.BigInteger


fun toNetworkInstructions(instructions: String, network: String): NetworkInstructions {
    return NetworkInstructions(
        instructions.map { c ->
            when(c) {
                'L' -> {it -> it.left}
                'R' -> {it -> it.right}
                else -> throw NotImplementedError("")
            }
        },
        network.lines().associate { toElement(it) }
    )
}

fun toElement(line: String): Pair<String, Elements> {
    val (key, v1, v2) = line.filter { it.isLetterOrDigit() || it == ' ' }.words().filter { it.isNotBlank() }
    return key to Elements(v1, v2)
}

data class NetworkInstructions(val instructions: List<(Elements) -> String>, val network: Map<String, Elements>) {
    fun countStepsUntil(start: String = "AAA", target: String): Int {
        return countStepsUntil(start) {it == target}
    }

    fun findFirstPointWhereAllPathsHitTarget(startChar: Char = 'A', target: Char = 'Z'): BigInteger {
        // As it turns out, to travel to the next Z from each Z again, it takes exactly the same amount of steps
        // So no need to get the Z to Z travel times

        return network.keys.filter { it.last() == startChar }
            .map { point -> countStepsUntil(point) { it.last() == target } }
            .leastCommonMultiple()

    }

    private fun countStepsUntil(start: String, matches: (String) -> Boolean): Int {
        var current = start
        return generateSequence { instructions }.flatten().map { take ->
            current = take(network[current]!!)
            current
        }.takeWhile { !matches(current) }.count() + 1
    }
}

data class Elements(val left: String, val right: String)

fun List<Int>.leastCommonMultiple() = this.map { it.toBigInteger() }.leastCommonMultiple()
