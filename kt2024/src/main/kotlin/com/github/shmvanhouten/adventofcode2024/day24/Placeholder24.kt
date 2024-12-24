package com.github.shmvanhouten.adventofcode2024.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode2024.day24.Operation.*

fun main() {
    readFile("/input-day24.txt")
        .lines()
        .onEach(::println)
}

fun simulateSystem(input: String): Long {
    val (
        gates: Map<String, Int>,
        connectionsByTarget: Map<String, Connection>
    ) = parseSystem(input)
    val knownGates = gates.toMutableMap()
    val unsolvedConnections = connectionsByTarget.keys.toMutableSet()
    while (unsolvedConnections.isNotEmpty()) {
        val nextConnection = unsolvedConnections.first { c ->
            val connection = connectionsByTarget[c]!!
            knownGates.contains(connection.first) && knownGates.contains(connection.second)
        }.let { connectionsByTarget[it]!! }
        unsolvedConnections.remove(nextConnection.target)
        knownGates[nextConnection.target] = nextConnection.invoke(knownGates)
    }
    return knownGates
        .filter { it.key.startsWith('z') }
        .entries.sortedByDescending { it.key }
        .map { it.value.digitToChar() }
        .joinToString("")
        .toLong(2)
}

private fun parseSystem(input: String): Pair<Map<String, Int>, Map<String, Connection>> {
    val (startingValues, conns) = input.blocks()
    val gates = startingValues.lines().map { it.split(": ") }.associate { (f, s) -> f to s.toInt() }
    val connections = parseConnections(conns.lines())
    return gates to connections
}

private fun parseConnections(lines: List<String>): Map<String, Connection> {
    val connections = lines.map { toConnection(it) }
    val reversed = connections.map { it.reverse() }
    return (connections + reversed).associateBy { it.target }
}

private fun toConnection(line: String): Connection {
    val (first, op, second, _, target) = line.split(" ")
    val operation: Operation = when (op) {
        "OR" -> OR
        "AND" -> AND
        "XOR" -> XOR
        else -> error("unknown operation: $op")
    }
    return Connection(first, second, operation, target)
}

data class Connection(
    val first: String,
    val second: String,
    val operation: Operation,
    val target: String
) {
    fun reverse(): Connection {
        return this.copy(first = second, second = first)
    }

    fun invoke(knownGates: MutableMap<String, Int>): Int {
        return operation.invocation.invoke(knownGates[first]!!, knownGates[second]!!)
    }

}

enum class Operation(val invocation: (Int, Int) -> Int) {
    OR(Int::or),
    AND(Int::and),
    XOR(Int::xor)
}
