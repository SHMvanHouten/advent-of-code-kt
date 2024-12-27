package com.github.shmvanhouten.adventofcode2024.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day24Test {

    @Nested
    inner class Part1 {

        @Test
        fun `example 0`() {
            val example = """
                x00: 1
                x01: 1
                x02: 1
                y00: 0
                y01: 1
                y02: 0

                x00 AND y00 -> z00
                x01 XOR y01 -> z01
                x02 OR y02 -> z02
            """.trimIndent()
            expectThat(simulateSystem(example)).isEqualTo(4)
        }

        @Test
        internal fun `example 1`() {
            expectThat(simulateSystem(example)).isEqualTo(2024)
        }

        @Test
        internal fun `part 1`() {
            expectThat(simulateSystem(input)).isEqualTo(42410633905894)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `playing with outputs`() {
            expectThat(play(input)).isEqualTo(-1)
        }

        @Test
        internal fun `part 2`() {
            val input = input
                .replace("x13 AND y13 -> z13", "mks XOR bhr -> z13")
                .replace("mks XOR bhr -> vcv", "x13 AND y13 -> vcv")

                .replace("csn XOR nmn -> vwp", "csn AND nmn -> vwp")
                .replace("csn AND nmn -> z19", "csn XOR nmn -> z19")

                .replace("vbw OR qkk -> z25", "mqj XOR pqn -> z25")
                .replace("mqj XOR pqn -> mps", "vbw OR qkk -> mps")

                .replace("x33 XOR y33 -> vjv", "x33 AND y33 -> vjv")
                .replace("x33 AND y33 -> cqm", "x33 XOR y33 -> cqm")

            expectThat(play(input)).isEqualTo(-1)

            val logicGateSystem = parseLogicGateSystem(input).setInput(4454, 27231)
            expectThat(logicGateSystem.simulate()).isEqualTo(31685)

            println(listOf(
                "z13",
                "vcv",
                "vwp",
                "z19",
                "z25",
                "mps",
                "vjv",
                "cqm",
            ).sorted().joinToString(","))
        }
    }

    private val input by lazy { readFile("/input-day24.txt")}
    private val example = """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1

        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
    """.trimIndent()
}
