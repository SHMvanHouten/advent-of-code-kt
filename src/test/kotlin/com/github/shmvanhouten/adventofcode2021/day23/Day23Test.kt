package com.github.shmvanhouten.adventofcode2021.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day23Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val example = """
                |#############
                |#...........#
                |###B#C#B#D###
                |  #A#D#C#A#
                |  #########""".trimMargin()
            val burrow = toAmphipodBurrow(example)
            assertThat(
                shortestPathToBurrowHappiness(burrow),
                equalTo(12521)
            )
        }

        @Test
        internal fun `part 1`() {
            assertThat(1, equalTo(1) )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy { readFile("/input-day23.txt")}

}
