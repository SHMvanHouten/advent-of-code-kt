package com.github.shmvanhouten.adventofcode2024.day17

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.math.pow

class Day17Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest(name = "{2}")
        @CsvSource(value = [
            "[5,3];[3];combo operand 3 is a literal 3",
            "[5,4];[6];combo operand 4 points to register A",
            "[5,5];[2];combo operand 5 points to register B, and value is mod 8 of that",
            "[5,6];[7];combo operand 6 points to register C, and value is mod 8 of that",
        ],
            delimiter = ';')
        fun `out instruction calculates the value of the combo operand mod 8`(
            prog: String,
            exp: String,
            explanation: String
        ) {
            val program = toList(prog)
            val expected = toList(exp)
            val computer =  Computer(6, 10, 15)
            expectThat(computer.run(program)).isEqualTo(expected)
        }

        @Test
        fun `opcode 0 (adv) divides a by the 2 to the power of the combo operand and sets it to register A`() {
            val program = listOf(0, 3, 5, 4)
            val computer = Computer(16, 0, 0)
            expectThat(computer.run(program)).isEqualTo(listOf(2))
            expectThat(computer.registerA).isEqualTo(2)

            val program2 = listOf(0, 5)
            val computer2 = Computer(64, 2, 0)
            computer2.run(program2)
            expectThat(computer2.registerA).isEqualTo(16)
        }

        @Test
        fun `opcode 1 (bxl) calculates the bitwise xor of b and stores it in b`() {
            val program = listOf(1, 7, 5, 5)
            val computer = Computer(0, 29, 0)
            val output = computer.run(program)
            expectThat(computer.registerB).isEqualTo(26)
            expectThat(output).isEqualTo(listOf(2))
        }

        @Test
        fun `opcode 2 (bst) stores combo operand mod 8 in b register`() {
            val program = listOf(2, 6)
            val computer = Computer(0, 0, 44)
            computer.run(program)
            expectThat(computer.registerB).isEqualTo(4)
        }

        @Test
        fun `jnz (3) jumps if A to the value of the literal operand is not 0`() {
            val program = listOf(3,4,5,1,5,2)
            val computer = Computer(1, 0, 0)
            expectThat(computer.run(program)).isEqualTo(listOf(2))
        }

        @Test
        fun `bxc (4) ignores operand and stores b xor c in register b`() {
            val program = listOf(4, 6)
            val computer = Computer(0, 1, 2)
            computer.run(program)
            expectThat(computer.registerB).isEqualTo(3)
        }

        @Test
        fun `bdv (6) is like adv but stored in register b`() {
            val program = listOf(6, 3, 5, 5)
            val computer = Computer(16, 0, 0)
            expectThat(computer.run(program)).isEqualTo(listOf(2))
            expectThat(computer.registerB).isEqualTo(2)

            val program2 = listOf(6, 5)
            val computer2 = Computer(64, 2, 0)
            computer2.run(program2)
            expectThat(computer2.registerB).isEqualTo(16)
        }

        @Test
        fun `cdv (7) is like adv but stored in register c`() {
            val program = listOf(7, 3, 5, 6)
            val computer = Computer(16, 0, 0)
            expectThat(computer.run(program)).isEqualTo(listOf(2))
            expectThat(computer.registerC).isEqualTo(2)

            val program2 = listOf(7, 5)
            val computer2 = Computer(64, 2, 0)
            computer2.run(program2)
            expectThat(computer2.registerC).isEqualTo(16)
        }

        @Test
        fun examples() {
            val computer = Computer(2024, 0, 0)
            val output = computer.run(listOf(0, 1, 5, 4, 3, 0))
            expectThat(output).isEqualTo(listOf(4,2,5,6,7,7,7,7,3,1,0))
            expectThat(computer.registerA).isEqualTo(0)
        }

        @Test
        internal fun `example 1`() {
            val (computer, program) = parseComputer(example)
            expectThat(computer.run(program)).isEqualTo(listOf(4,6,3,5,6,3,5,2,1,0))
        }

        @Test
        internal fun `part 1`() {
            val (computer, program) = parseComputer(input)
            expectThat(computer.run(program)).isEqualTo(listOf(4,1,5,3,1,5,3,5,7))
        }
//
//        @Test
//        fun `simplified for input`() {
//            val (computer, program) = parseComputer(input)
//            val regA = computer.registerA.toString(8)
//            expectThat(simplified(regA)).isEqualTo("415315357")
//        }
    }

    @Nested
    inner class Part2 {

        fun simpleProgram(aReg: Int): Int {
            var bReg = aReg % 8
            bReg = bReg.xor(1)
            val cReg = (aReg / 2.0.pow(bReg)).toInt()
            bReg = bReg.xor(5)
            expectThat(bReg.xor(cReg) % 8).isEqualTo(bReg.xor(cReg % 8))
            bReg = bReg.xor(cReg)
            return bReg % 8
        }

        @Test
        fun testSimpleProgram() {
            (0..100).forEach {
                println("$it to ${simpleProgram(it)}")
            }
        }

        @Test
        fun `part 2`() {
            val cracked = crackInput(input)
            println(cracked)
            val (computer, program) = parseComputer(input)
            computer.registerA = 164542125272765
            expectThat(computer.run(program)).isEqualTo(listOf(2, 4, 1, 1, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0))
        }

        // [2, 4, 1, 1, 7, 5, 1, 5, 0, 3, 4, 3, 5, 5, 3, 0]

        //2 bst: registerB = registerA % 8                  last digit of regA
        //1 bxl: registerB = registerB.xor(1)               flip the three bits
        //7 cdv: registerC = RegisterA / 2^registerB        dividing by some power of 2 -> right shift that amount -> *1 get the last 3 bits that remain
        //1 bxl: registerB = registerB.xor(5)               xor 011
        //0 adv: registerA = RegisterA / 2^3                right shift by 3 -> remove last base 8 digit
        //4 bxc: registerB = registerB.xor(registerC)       (*1) since we only need the last 3 bits when we print,
        //5 out: print (registerB % 8)                      only the last 3 bits of regC are relevant for the xor
        //3 jnz: jmp to 0 if regA != 0
        @Test
        internal fun `part 2 doodles`() {
            val (c, program) = parseComputer(input)
//            println(program)
//            println(c.explain(program))
//            (0..7L).forEach {
//                val computer = c.copy()
//                computer.registerA = it
//                println("${it + 1}: ${computer.run(program)}")
//            }
            //                                 2411751503435530
            //                                 0355343051571142
            program.reversed().windowed(2).forEach {
                val results = ("10".toLong(8).."77".toLong(8)).map { l ->
                    val computer = c.copy(registerA = l)

                    val result = computer.run(program)
                    l to result
                }.filter { (_, r) -> r == it.reversed() }
                if(results.isEmpty()) {
                    println("error")
                } else {
                    results.forEach{ (l, result) -> println("at ${l.toString(8)}: $result")}
                }
            }
            val computer = c.copy(registerA = "4717076506024416".toLong(8) )
            val result = computer.run(program)
            println(result)
            println(result.count())
//            (0..Int.MAX_VALUE).forEach {
//                val computer = c.copy()
//                computer.registerA = it.toLong()
//                val result = computer.run(program)
//                if(result == program) {
//                    println("found it!")
//                    println("for register A = $it")
//                    println("phew")
//                } else {
//                    if(result.size >= 7 && result.subList(0, 6) == listOf(2,4,1,1,7,5)) {
//                        println("$it: $result")
//                    }
//                }
//            }
        }

    }

    private fun toList(input: String): List<Int> {
        return input.substringBetween("[", "]").split(",").map { it.toInt() }
    }

    private val input by lazy { readFile("/input-day17.txt")}
    private val example = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent()
}
