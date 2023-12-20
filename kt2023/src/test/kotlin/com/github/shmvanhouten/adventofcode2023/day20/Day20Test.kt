package com.github.shmvanhouten.adventofcode2023.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.compositenumber.leastCommonMultiple
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day20Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val example = """
                broadcaster -> a, b, c
                %a -> b
                %b -> c
                %c -> inv
                &inv -> a
            """.trimIndent()
            val machine = Machine(example)
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(8)
                that(machine.highCount).isEqualTo(4)
            }
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(16)
                that(machine.highCount).isEqualTo(8)
            }
        }

        @Test
        internal fun `example 1 1000 pushes`() {
            val example = """
                broadcaster -> a, b, c
                %a -> b
                %b -> c
                %c -> inv
                &inv -> a
            """.trimIndent()
            val machine = Machine(example)
            repeat(1000) {machine.button() }
            expect {
                that(machine.lowCount).isEqualTo(8000)
                that(machine.highCount).isEqualTo(4000)
            }
        }

        @Test
        fun `example 2`() {
            val example = """
                broadcaster -> a
                %a -> inv, con
                &inv -> b
                %b -> con
                &con -> output
            """.trimIndent()
            val machine = Machine(example)
            validateExample2Cycle(machine, 0, 0)
            validateExample2Cycle(machine, machine.highCount, machine.lowCount)
        }

        private fun validateExample2Cycle(machine: Machine, highBefore: Int, lowBefore: Int) {
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(lowBefore + 4)
                that(machine.highCount).isEqualTo(highBefore + 4)
            }
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(lowBefore + 4 + 4)
                that(machine.highCount).isEqualTo(highBefore + 4 + 2)
            }
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(lowBefore + 4 + 4 + 5)
                that(machine.highCount).isEqualTo(highBefore + 4 + 2 + 3)
            }
            machine.button()
            expect {
                that(machine.lowCount).isEqualTo(lowBefore + 4 + 4 + 5 + 4)
                that(machine.highCount).isEqualTo(highBefore + 4 + 2 + 3 + 2)
            }
        }

        @Test
        fun `example 2 1000 pushes`() {
            val example = """
                broadcaster -> a
                %a -> inv, con
                &inv -> b
                %b -> con
                &con -> output
            """.trimIndent()
            val machine = Machine(example)
            repeat(1000) {machine.button()}
            expect {
                that(machine.lowCount).isEqualTo(4250)
                that(machine.highCount).isEqualTo(2750)
            }
        }

        @Test
        internal fun `part 1`() {
            val machine = Machine(input)
            repeat(1000) {machine.button()}
            expectThat(machine.lowCount.toLong() * machine.highCount).isEqualTo(787056720)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        fun `in order to hit rx, we must press the button x times`() {
            val input = """
                &ql -> rx
                &fh -> ql
                &mf -> ql
                broadcaster ->
            """.trimIndent()
        }

        @Test
        fun `turn input into directed graph visualisation`() {
            val modules = Machine(input).modules
            val moduleIdsAndType = modules.values.associate { it.id to "${it.id}${it.type}" }
            println("digraph {")
            modules.values.map { "\"${moduleIdsAndType[it.id]}\" -> ${it.targets.map { "\"" + (moduleIdsAndType[it] ?: it) + "\"" }.joinToString(", ")}" }
                .onEach(::println)
            println("}")
            // From command line run:
            // brew install graphviz
            // dot -Tsvg scratch_116.txt > day20.svg
        }

        @Test
        @Disabled("runs very very long, just used for getting the lcms")
        internal fun `part 2`() {
            // 0110 28530946 - 14265473 = 14265473
            // 1100 28937134 - 14468567 = 14468567
            // 1010 29183342 - 14591671 = 14591671
            // 0101 43789323 - 14596441 - 14596441 = 14596441
            // 0011 29441266 - 14720633 = 14720633
            // 1001 14930207
            println(leastCommonMultiple(listOf(14265473, 14930207)))
            println(leastCommonMultiple(listOf(14468567, 14720633)))
            val machine = Machine(input)
            for (i in 1..Int.MAX_VALUE) {
                if(machine.button()) error(i)
            }
        }
    }

    private val input by lazy { readFile("/input-day20.txt")}

}
