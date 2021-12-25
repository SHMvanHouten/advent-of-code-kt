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
            val energy = shortestPathToBurrowHappiness(burrow)
            assertThat(
                energy,
                equalTo(12521)
            )
        }

        @Test
        internal fun `part 1`() {
            val burrow = toAmphipodBurrow(input)
            val energy = shortestPathToBurrowHappiness(burrow)
            assertThat(
                energy,
                equalTo(19167)
            )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val example = """#############
#...........#
###B#C#B#D###
  #D#C#B#A#
  #D#B#A#C#
  #A#D#C#A#
  #########"""
            val burrow = toAmphipodBurrow(example)
            val energy = shortestPathToBurrowHappiness(burrow)
            assertThat(
                energy,
                equalTo(44169)
            )

        }

        @Test
        internal fun `part 2`() {
            val input = """#############
#...........#
###D#A#C#A###
  #D#C#B#A#
  #D#B#A#C#
  #D#C#B#B#
  #########"""
            val burrow = toAmphipodBurrow(input)
            val energy = shortestPathToBurrowHappiness(burrow)
//            47283 too low
//            47857 too high
            assertThat(
                energy,
                equalTo(19167)
            )
        }
    }

    private val input by lazy { readFile("/input-day23.txt")}

}
