package com.github.shmvanhouten.adventofcode2024.day24

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode2024.day24.Operation.*


fun simulateSystem(input: String): Long {
    return parseLogicGateSystem(input).simulate()
}

fun play(input: String): Long {
    val system = parseLogicGateSystem(input)
    val connectionsByOrigin = system.connectionsByOrigin

    val xInputs = system.inputBits.keys
        .filter { it.startsWith("x") }.sorted()
    var overflowBit = checkFirstHalfAdder(connectionsByOrigin, xInputs)
    for (gate in xInputs.tail()) {
        assert(overflowBit.isNotEmpty()) { "missing overflow, calculating from z" }
        val connections = connectionsByOrigin[gate]!!
        if (!connections.map { it.operation }.containsAll(listOf(XOR, AND))) {
            println("failed on x and y connection did not contain XOR and AND: $connections")
        }

        val xXORy = connections.first { it.operation == XOR }.target
        val secondXorAndAnd = connectionsByOrigin[xXORy]!!
        val secondAnd = if (!secondXorAndAnd.map { it.operation }.containsAll(listOf(XOR, AND))) {
            println("$gate: secondXorAndAnd did not contain both XOR and AND $secondXorAndAnd")
            if (gate == "x33") {
                println("switch y33 OR x33 with x33 AND y33")
                overflowBit = "qnw"
                continue
            }
            error("There is a broken logic circuit at $gate")
        } else {
            val xorToZ = secondXorAndAnd.first { it.operation == XOR }
            if (!(xorToZ.anyGateInputEquals(overflowBit)) || xorToZ.target != gate.currentZ()) {
                println("$gate: failed on XOR (overflow, x XOR y) -> z $xorToZ")
                overflowBit = if (gate == "x13") {
                    println("switch y13 AND x13 -> z13 with bhr XOR mks -> vcv")
                    "ngh"
                } else if (gate == "x19") {
                    println("switch nmn XOR csn -> z19 with nmn AND csn -> vwp")
                    "dhf"
                } else if (gate == "x25") {
                    println("switch qkk OR vbw -> z25 with pqn XOR mqj -> mps")
                    "mps"
                } else {
                    error("There is a broken logic circuit at $gate")
                }

                continue
            }
            val secondAnd = secondXorAndAnd.first { it.operation == AND }
            if (!(secondAnd.anyGateInputEquals(overflowBit))) {
                println("failed on second AND, was not with overflow $secondAnd")
                error("There is a broken logic circuit at $gate")
            } else {
                secondAnd.target
            }
        }
        val xAndY = connections.first { it.operation == AND }.target
        val theOrList = connectionsByOrigin[xAndY]!!
        overflowBit = if (theOrList.size != 1) {
            println("expected first xor to output into single OR, instead: $theOrList")
            ""
        } else {
            val theOr = theOrList.single()
            if (!(theOr.anyGateInputEquals(secondAnd))) {
                println("expected theOr to be between secondAnd and firstAnd, was instead: $theOr")
                ""
            } else {
                theOr.target
            }
        }

    }
    return -1
}

private fun checkFirstHalfAdder(
    connectionsByOrigin: Map<String, List<Connection>>,
    xInputs: List<String>
): String {
    val x00 = xInputs.first()
    assert(x00 == "x00") { "expected first input bit to be x00, instead was: $x00" }
    val logicGates = connectionsByOrigin[x00]!!
    assert(logicGates.map { it.operation }.containsAll(listOf(XOR, AND))) {
        "expected logic gates between x00, y00 and z00 to be an AND and an OR $logicGates"
    }
    assert(logicGates.flatMap { it.inputs }.count{ it == "y00"} == 2) {
        "expected both logic gates for half adder to be from x00 and y00 $logicGates"
    }
    assert(logicGates.first { it.operation == XOR }.target == "z00") {
        "expected logic gate for x00 XOR y00 to go to z00, instead went to ${logicGates.first { it.operation == XOR }.target}"
    }
    return logicGates.first { it.operation == AND }.target
}

private fun String.currentZ(): String {
    return replace('x', 'z')
}

fun parseLogicGateSystem(input: String): LogicGateSystem {
    val (startingValues, conns) = input.blocks()
    return LogicGateSystem(
        startingValues.lines().map { it.split(": ") }
            .associate { (f, s) -> f to s.toInt() },
        conns.lines().map { toConnection(it) }.toSet()
    )
}

private fun toConnection(line: String): Connection {
    val (first, op, second, _, target) = line.split(" ")
    val operation: Operation = when (op) {
        "OR" -> OR
        "AND" -> AND
        "XOR" -> XOR
        else -> error("unknown operation: $op")
    }
    return Connection(listOf(first, second), operation, target)
}

data class Connection(
    val inputs: List<String>,
    val operation: Operation,
    val target: String
) {

    fun invoke(knownGates: MutableMap<String, Int>): Int {
        return operation.invocation.invoke(knownGates[inputs.first()]!!, knownGates[inputs[1]]!!)
    }

    fun anyGateInputEquals(logicGateInput: String): Boolean = inputs.any { it == logicGateInput }
}

enum class Operation(val invocation: (Int, Int) -> Int) {
    OR(Int::or),
    AND(Int::and),
    XOR(Int::xor)
}

data class LogicGateSystem(
    val inputBits: Map<String, Int>,
    val logicGates: Set<Connection>
) {

    fun setInput(x: Long, y: Long): LogicGateSystem {
        val xAsBinary = x.toString(2).reversed()
        val yAsBinary = y.toString(2).reversed()
        val inputBits = mutableMapOf<String, Int>()
        0.until(this.inputBits.keys.filter { it.startsWith("x") }.size).forEach {
            val (xnn, ynn) = "x${it.toString().padStart(2,'0')}" to "y${it.toString().padStart(2,'0')}"
            inputBits[xnn] = xAsBinary.getOrNull(it)?.digitToInt()?:0
            inputBits[ynn] = yAsBinary.getOrNull(it)?.digitToInt()?:0
        }
        return copy(inputBits = inputBits)
    }

    fun simulate(): Long {
        val knownInputs = inputBits.toMutableMap()
        val unsolvedConnections = logicGates.toMutableSet()
        while (unsolvedConnections.isNotEmpty()) {
            val nextConnection = unsolvedConnections.first { c ->
                c.inputs.all { knownInputs.contains(it) }
            }
            unsolvedConnections.remove(nextConnection)
            knownInputs[nextConnection.target] = nextConnection.invoke(knownInputs)
        }
        return knownInputs
            .filter { it.key.startsWith('z') }
            .entries.sortedByDescending { it.key }
            .map { it.value.digitToChar() }
            .joinToString("")
            .toLong(2)
    }

    val connectionsByOrigin: Map<String, List<Connection>> by lazy {
        val connectionsByInput = mutableMapOf<String, List<Connection>>()
        logicGates.forEach {
            it.inputs.forEach { input ->
                connectionsByInput.merge(input, listOf(it), List<Connection>::plus)
            }
        }
        connectionsByInput.toMap()
    }
}

fun <T> List<T>.tail() = subList(1, size)