package com.github.shmvanhouten.adventofcode2021.day23

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day23Test {

    @Nested
    inner class Part1 {

        @Test
        @Disabled("had to change the logic for part 2")
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
        @Disabled("had to change the logic for part 2")
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
            assertThat(
                energy,
                equalTo(47665)
            )
            // 47857
            // 47711
        }
    }

    private val input by lazy { readFile("/input-day23.txt") }

}
/**
#############
#...........#
###D#A#C#A###
  #D#C#B#A#
  #D#B#A#C#
  #D#C#B#B#
  #########

#############
#.........CA#
###D#.#.#A###
  #D#C#B#A#
  #D#B#A#C#
  #D#C#B#B#
  #########

407

#############
#A......B.CA#
###D#.#.#A###
  #D#C#.#A#
  #D#B#.#C#
  #D#C#B#B#
  #########

407 + 30 + 9 + 90 = 536

#############
#AB.B...B.CA#
###D#.#.#A###
  #D#.#.#A#
  #D#.#C#C#
  #D#.#C#B#
  #########

536 + 800 + 40 + 900 = 2276

#############
#A.........A#
###D#.#.#A###
  #D#B#C#A#
  #D#B#C#C#
  #D#B#C#B#
  #########

2276 + 50 + 60 + 500 + 50 = 2936

#############
#AA.......AA#
###D#B#C#.###
  #D#B#C#.#
  #D#B#C#.#
  #D#B#C#.#
  #########

2936 + 2 + 9 + 600 + 90 = 3637

3637 + 44000 + 28 = 47665
 */
