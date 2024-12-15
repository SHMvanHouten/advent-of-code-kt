package com.github.shmvanhouten.adventofcode2024.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigInteger

class Day13Test {

    /**
     *  butA    butB  prize     butA    butB  prize
     *     x       x      x        y       y      y
     * n *94 + m *22 = 8400 && n *34 + m *67 = 5400
     * 80      40
     * 8400 - m * 22
     *
     * n = (8400 - 22m) / 94 = (5400 - 67m) / 34
     * 3196/ 94 * (8400 - 22m) = 3196 / 34 * (5400 - 67m)
     *
     * butA.y * (prize.x - butB.x * m) = butA.x * (prize.y - butB.y * m)
     * 34 * (8400 - 22m) = 94 * (5400 - 67m)
     *
     * butA.y * prize.x - butA.y * butB.x * m = butA.x * prize.y - butA.x * butB.y * m
     * (34 * 8400) - (34 * 22 * m) = (94 * 5400) - (94 * 67 * m)
     * 285.600     -  748 * m      =  507.600    -  6298 * m
     *
     * butA.y * prize.x - (butA.y * butB.x - butA.x * butB.y) * m = butA.x * prize.y
     * (34 * 8400) - (34 * 22 - 94 * 67) * m = 94 * 5400
     *  285.600 + 5.550m = 507.600
     *
     * (butA.y * butB.x - butA.x * butB.y) * m = butA.x * prize.y - butA.y * prize.x
     * (34 * 22 - 94 * 67) * m = 94 * 5400 - 34 * 8400
     *  5.550m = 507.600 - 285.600
     *           222.000
     *
     * m = (buttonA.x * prize.y - buttonA.y * prize.x) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
     *
     * - (buttonA.y * buttonB.x - buttonA.x * buttonB.y) * m = (buttonA.x * prize.y) - (buttonA.y * prize.x)
     * m = (buttonA.x * prize.y) - (buttonA.y * prize.x) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
     *
     * 142.800 - 374m = 253.800 - 3.149m
     * - 374m = 111000 - 3149m
     * m = 111000/ 2775
     * m = 40
     *
     * (butA.x * butA.y)
     */

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val clawMachines = parseClawMachines(example)
            expectThat(calculateMinimumTokensToSpendToGetToPrize(clawMachines))
                .isEqualTo(BigInteger.valueOf(480))

        }

        @Test
        internal fun `part 1`() {
            val clawMachines = parseClawMachines(input)
            expectThat(calculateMinimumTokensToSpendToGetToPrize(clawMachines))
                .isEqualTo(BigInteger.valueOf(29023))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            val clawMachines = parseClawMachines(input)
                .map { it.copy(prize = it.prize.plus(BigInteger.valueOf(10000000000000))) }
            expectThat(calculateMinimumTokensToSpendToGetToPrize(clawMachines))
                .isEqualTo(BigInteger.valueOf(96787395375634))
        }
    }

    private val input by lazy { readFile("/input-day13.txt")}
    private val example = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent()
}
