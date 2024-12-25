package com.github.shmvanhouten.adventofcode2024.day24

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode2024.day24.Operation.*

fun play(input: String): Long {
    val (
        gates: Map<String, Int>,
        connectionsByTarget: Map<String, Connection>,
        connectionsByOrigin: Map<String, List<Connection>>
    ) = parseSystem(input)

    var overflowBit = ""
    for (gate in gates.keys.sorted()
        .filter { it.startsWith("x") }) {
        println(gate)
        val connections = connectionsByOrigin[gate]!!
        if(!connections.map { it.operation }.containsAll(listOf(XOR, AND))) {
            println("failed on x and y connection did not contain XOR and AND: $connections")
        }
        if(gate == "x00") {
            overflowBit = connections.first { it.operation == AND }.target
        } else {

            if (overflowBit == "") {
                println("missing overflow, calculating from z")
            }

            val xXORy = connections.first { it.operation == XOR }.target
            val secondXorAndAnd = connectionsByOrigin[xXORy]!!
            val secondAnd = if(!secondXorAndAnd.map { it.operation }.containsAll(listOf(XOR, AND))) {
                println("secondXorAndAnd did not contain both XOR and AND $secondXorAndAnd")
                if(gate == "x33") {
                    println("switch y33 OR x33 with x33 AND y33")
                    overflowBit = "qnw"
                    continue
                }
                ""
            } else {
                val xorToZ = secondXorAndAnd.first { it.operation == XOR }
                val (first, second, _, target) = xorToZ
                if (!(second == overflowBit || first == overflowBit) || target != gate.currentZ()) {
                    println("failed on XOR (overflow, x XOR y) -> z $xorToZ")
                    overflowBit = if(gate == "x13") {
                        println("switch y13 AND x13 -> z13 with bhr XOR mks -> vcv")
                        "ngh"
                    } else if(gate == "x19") {
                        println("switch nmn XOR csn -> z19 with nmn AND csn -> vwp")
                        "dhf"
                    } else if(gate == "x25") {
                        println("switch qkk OR vbw -> z25 with pqn XOR mqj -> mps")
                        "mps"
                    } else {
                        ""
                    }

                    continue
                }
                val secondAnd = secondXorAndAnd.first { it.operation == AND }
                if(!(secondAnd.first == overflowBit || secondAnd.second == overflowBit)) {
                    println("failed on second AND, was not with overflow $secondAnd")
                    ""
                } else {
                    secondAnd.target
                }
            }
            val xAndY = connections.first { it.operation == AND }.target
            val theOrList = connectionsByOrigin[xAndY]!!
            overflowBit = if(theOrList.size != 1) {
                println("expected first xor to output into single OR, instead: $theOrList")
                ""
            } else {
                val theOr = theOrList.single()
                if(!(theOr.first == secondAnd || theOr.second == secondAnd)) {
                    println("expected theOr to be between secondAnd and firstAnd, was instead: $theOr")
                    ""
                } else {
                    theOr.target
                }
            }

        }
    }
    return -1
}

private fun String.currentZ(): String {
    return replace('x', 'z')
}

private fun String.nextZ(): String {
    val nr = (this.substring(1).toInt() + 1).toString().padStart(2, '0')
    return "z$nr".also { println(it) }
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

private fun parseSystem(input: String): Triple<Map<String, Int>, Map<String, Connection>, Map<String, List<Connection>>> {
    val (startingValues, conns) = input.blocks()
    val gates = startingValues.lines().map { it.split(": ") }.associate { (f, s) -> f to s.toInt() }
    val (connectionsByTarget, connectionsByOrigin) = parseConnections(conns.lines())
    return Triple(gates, connectionsByTarget, connectionsByOrigin)
}

private fun parseConnections(lines: List<String>): Pair<Map<String, Connection>, Map<String, List<Connection>>> {
    val connections = lines.map { toConnection(it) }
    val reversed = connections.map { it.reverse() }
    val both = connections + reversed

    val connectionsByTarget = mutableMapOf<String, List<Connection>>()
    connections.forEach {
        val (first, second) = it
        connectionsByTarget.merge(first, listOf(it), List<Connection>::plus)
        connectionsByTarget.merge(second, listOf(it), List<Connection>::plus)
    }
    return both.associateBy { it.target } to connectionsByTarget.toMap()
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
