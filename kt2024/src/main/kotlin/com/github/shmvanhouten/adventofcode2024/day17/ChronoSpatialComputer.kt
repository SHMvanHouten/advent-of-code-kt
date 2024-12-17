package com.github.shmvanhouten.adventofcode2024.day17

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import kotlin.math.pow

fun parseComputer(input: String): Pair<Computer, List<Int>> {
    val (computer, program) = input.blocks()
    return toComputer(computer) to program.substringAfter(": ").split(",").map { it.toInt() }
}

fun crackInput(input: String): Long {
    val (c, program) = parseComputer(input)
    var resultsSoFar = listOf("")
    program.indices.reversed().forEach {index ->
        val target = program.subList(index, program.size)
        resultsSoFar = resultsSoFar.flatMap {resultSoFar ->
            (0..7L).filter {
                val computer = c.copy(registerA = (resultSoFar + it).toLong(8))
                computer.run(program) == target
            }.map { resultSoFar + it }
        }

    }
    println(resultsSoFar)
    return resultsSoFar.min().toLong(8)
}

fun simplified(aReg: Int): Int {
    var bReg = aReg % 8
    bReg = bReg.xor(1)
    val cReg = (aReg / 2.0.pow(bReg)).toInt()
    bReg = bReg.xor(5)
    bReg = bReg.xor(cReg)
    return bReg % 8
//    var registerA = regA
//    return buildString {
//        while (registerA.isNotEmpty()) {
//            val registerB = regA.last().digitToInt().xor(1)
//            val registerC = (registerA.toLong(8) / 2.0.pow(registerB)).toInt() % 8
//            append(registerB.xor(5).xor(registerC))
//            registerA = registerA.dropLast(1)
//        }
//    }
}

data class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long
) {
    fun run(program: List<Int>): List<Int> {
        var pointer = 0
        return buildList {
            while (pointer <= program.lastIndex - 1) {
                val opcode = program[pointer]
                val operand = program[pointer + 1]
                when(opcode) {
                    0 -> registerA /= 2.0.pow(combo(operand).toDouble()).toLong()
                    1 -> registerB = registerB.xor(literal(operand))
                    6 -> registerB = registerA / 2.0.pow(combo(operand).toDouble()).toLong()
                    7 -> registerC = registerA / 2.0.pow(combo(operand).toDouble()).toLong()

                    2 -> registerB = combo(operand) % 8
                    3 -> {
//                        println("\n${registerA.toString(8)}, printed: ${this.last()}, opcode + operand: ${explain(opcode, operand)}\n")
                    }
                    4 -> registerB = registerB.xor(registerC)
                    5 -> add((combo(operand) % 8).toInt())
                    else -> error("bad opcode")
                }
                pointer = jmp(pointer, opcode, operand)
//                println("${this@Computer}, opcode + operand: ${explain(opcode, operand)}")
            }
        }
    }

    private fun jmp(pointer: Int, opcode: Int, operand: Int): Int {
        return if(opcode == 3 && registerA != 0L) {
            operand
        } else {
            pointer + 2
        }
    }

    private fun literal(operand: Int): Long = operand.toLong()

    fun explain(program: List<Int>): String {
        return program.windowed(2, 2).joinToString("\n") { (opcode, operand) ->
            explain(opcode, operand)
        }
    }

    private fun explain(opcode: Int, operand: Int) = when (opcode) {
        0 -> "0 adv: registerA = RegisterA / 2^${comboString(operand)}"
        6 -> "6 bdv: registerB = RegisterA / 2^${comboString(operand)}"
        7 -> "7 cdv: registerC = RegisterA / 2^${comboString(operand)}"

        1 -> "1 bxl: registerB = registerB.xor($operand)"
        2 -> "2 bst: registerB = ${comboString(operand)} % 8"
        3 -> "3 jnz: jmp to $operand if regA != 0"
        4 -> "4 bxc: registerB = registerB.xor(registerC)"
        5 -> "5 out: print (${comboString(operand)} % 8)"
        else -> error("bad opcode")
    }

    private fun comboString(operand: Int): String {
        return when(operand) {
            0, 1, 2, 3 -> operand.toString()
            4 -> "registerA"
            5 -> "registerB"
            6 -> "registerC"
            else -> error("invalid operand: $operand")
        }
    }

    private fun combo(operand: Int): Long {
        return when(operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> error("invalid operand: $operand")
        }
    }

    override fun toString(): String {
        return "Computer: A=${registerA.toString(8)}, B=${registerB.toString(8)}, C=${registerC.toString(8)}"
    }
}

private fun toComputer(computer: String): Computer {
    val (a, b, c) = computer.lines()
        .map { it.substringAfter(": ") }
        .map { it.toLong() }
    return Computer(a, b, c)
}
