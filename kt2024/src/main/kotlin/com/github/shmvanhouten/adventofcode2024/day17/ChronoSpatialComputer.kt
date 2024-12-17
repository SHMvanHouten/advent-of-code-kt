package com.github.shmvanhouten.adventofcode2024.day17

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import kotlin.math.pow

fun parseComputer(input: String): Pair<Computer, List<Int>> {
    val (computer, program) = input.blocks()
    return toComputer(computer) to program.substringAfter(": ").split(",").map { it.toInt() }
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
                    3 -> {/*no register changes*/}
                    4 -> registerB = registerB.xor(registerC)
                    5 -> add((combo(operand) % 8).toInt())
                    else -> error("bad opcode")
                }
                pointer = jmp(pointer, opcode, operand)
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

    private fun combo(operand: Int): Long {
        return when(operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> error("invalid operand: $operand")
        }
    }
}

private fun toComputer(computer: String): Computer {
    val (a, b, c) = computer.lines()
        .map { it.substringAfter(": ") }
        .map { it.toLong() }
    return Computer(a, b, c)
}
