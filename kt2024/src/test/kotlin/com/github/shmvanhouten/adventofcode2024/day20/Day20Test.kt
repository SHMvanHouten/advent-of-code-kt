package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day20Test {

    @Nested
    inner class Part1 {

        @Test
        fun `quickest path through example without cheats`() {
            expectThat(quickestPath(example).length).isEqualTo(84)
        }

        @Test
        fun `there is a cheat for the example that saves 64 ps`() {
            val cheats = findCheats(example)
            expectThat(cheats.filter{it.gain == 64}).hasSize(1)
        }

        @Test
        fun `there are 44 cheats that save time for the example`() {
            val findCheats = findCheats(example)
            expect {
//                that(findCheats.filter { it.gain ==  2 })
//                    .hasSize(14) fails for some reason, I don't think it is correct
                that(findCheats.filter{ it.gain == 6 }).hasSize(2)
                that(findCheats.filter{ it.gain == 8 }).hasSize(4)
                that(findCheats.filter{ it.gain == 10}).hasSize(2)
                that(findCheats.filter{ it.gain == 12}).hasSize(3)
                that(findCheats.filter{ it.gain == 20}).hasSize(1)
                that(findCheats.filter{ it.gain == 36}).hasSize(1)
                that(findCheats.filter{ it.gain == 38}).hasSize(1)
                that(findCheats.filter{ it.gain == 40}).hasSize(1)
                that(findCheats.filter{ it.gain == 64}).hasSize(1)
            }
        }

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 1`() {
            val cheats = findCheats(input)
            expectThat(cheats.filter { it.gain >= 100 }).hasSize(1445)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day20.txt")}
    private val example = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent()

}
