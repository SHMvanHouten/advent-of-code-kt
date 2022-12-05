package com.github.shmvanhouten.adventofcode2022.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day05Test {

    private val example = """
                |    [D]    
                |[N] [C]    
                |[Z] [M] [P]
                | 1   2   3 
                |
                |move 1 from 2 to 1
                |move 3 from 1 to 3
                |move 2 from 2 to 1
                |move 1 from 1 to 2
            """.trimMargin()


    @Nested
    inner class Part1 {

        @Test
        internal fun `input is parsed`() {
            val (stacks, instructions) = parse(example)
            assertThat(stacks[1]).hasSize(2)
            assertThat(stacks[2]).hasSize(3)
            assertThat(stacks[2]!!.last()).isEqualTo('D')

            assertThat(instructions).hasSize(4)
            assertThat(instructions.first()).isEqualTo(Instruction(amount = 1, from = 2, to = 1))
        }

        @Test
        internal fun example() {
            val (stacks, instructions) = parse(example)
            val result = stacks.execute(instructions)

            assertThat(result.getTopCrates()).isEqualTo("CMZ")
        }

        @Test
        internal fun `part 1`() {
            val (stacks, instructions) = parse(input)
            val result = stacks.execute(instructions)

            assertThat(result.getTopCrates()).isEqualTo("CNSZFDVLJ")
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val (stacks, instructions) = parse(example)

            val result = stacks.executeV9001(instructions)
            assertThat(result.getTopCrates()).isEqualTo("MCD")
        }

        @Test
        internal fun `part 2`() {
            val (stacks, instructions) = parse(input)
            val result = stacks.executeV9001(instructions)

            assertThat(result.getTopCrates()).isEqualTo("QNDWLMGNS")
        }
    }

    private val input by lazy { readFile("/input-day05.txt")}

}
