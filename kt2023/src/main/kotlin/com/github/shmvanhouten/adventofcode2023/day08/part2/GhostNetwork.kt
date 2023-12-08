package com.github.shmvanhouten.adventofcode2023.day08.part2

import com.github.shmvanhouten.adventofcode.utility.compositenumber.leastCommonMultiple
import com.github.shmvanhouten.adventofcode.utility.strings.words
import com.github.shmvanhouten.adventofcode2023.day08.Elements
import java.math.BigInteger


fun toNetworkInstructions(instructions: String, network: String): NetworkInstructions {
    return NetworkInstructions(
        instructions.map { c ->
            when (c) {
                'L' -> { it -> it.left }
                'R' -> { it -> it.right }
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

    fun findFirstPointWhereAllPathsHitTarget(startChar: Char = 'A', target: Char = 'Z'): BigInteger {
        val startingPoints = network.keys.filter { it.last() == startChar }

        val travelTimesToFirstZ = startingPoints.map { traverseUntil(it, target) }.onEach { println(it) }

        var map = travelTimesToFirstZ.map { it.first }.map { it.toBigInteger() }
        return map.leastCommonMultiple()
//        while(greatestCommonDivisor != BigInteger.ONE) {
//            map = map.map { it.divide(greatestCommonDivisor) }
//            greatestCommonDivisor = map.greatestCommonDivisor()
//        }
//        return map.reduce(BigInteger::multiply)

    }

    fun traverseUntil(start: String, target: Char): Pair<Int, String> {
        var current = start
        var traversed = 0
        while (true) {
            instructions.forEach { take ->
                current = take(network[current]!!)
                traversed++
                if (current.last() == target) return traversed to current
            }
        }
    }
}

