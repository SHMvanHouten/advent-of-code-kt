package com.github.shmvanhouten.adventofcode2022.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day24Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `the map lazily evaluates`() {
            val input = """
                #.#####
                #.....#
                #>....#
                #.....#
                #...v.#
                #.....#
                #####.#
            """.trimIndent()
            val blizzardMap = BlizzardMap(input)
            expectThat(blizzardMap.getState(0).draw()).isEqualTo(input)
            val mapState: MapState = blizzardMap.getState(1)
            expectThat(mapState.draw())
                .isEqualTo("""
                    #.#####
                    #.....#
                    #.>...#
                    #.....#
                    #.....#
                    #...v.#
                    #####.#
                """.trimIndent())
        }

        @Test
        fun `blizzards move to the opposite side if they hit the wall`() {
            val input = """
                #.#####
                #.....#
                #>....#
                #.....#
                #...v.#
                #.....#
                #####.#
            """.trimIndent()
            val blizzardMap = BlizzardMap(input)
            blizzardMap.getState(1) // so that we can get 2
            val state = blizzardMap.getState(2)
            expectThat(state.draw()).isEqualTo("""
                #.#####
                #...v.#
                #..>..#
                #.....#
                #.....#
                #.....#
                #####.#
            """.trimIndent())
        }

        @Test
        fun `blizzard can occupy the same space`() {
            val input = """
                #.#####
                #...v.#
                #..>..#
                #.....#
                #.....#
                #.....#
                #####.#
            """.trimIndent()
            val blizzardMap = BlizzardMap(input)
            blizzardMap.getState(1) // so that we can get 2
            val state = blizzardMap.getState(2)
            expectThat(state.draw()).isEqualTo("""
                #.#####
                #.....#
                #....>#
                #...v.#
                #.....#
                #.....#
                #####.#
            """.trimIndent())
        }

        @Test
        fun example() {
            val input = """
                #.######
                #>>.<^<#
                #.<..<<#
                #>v.><>#
                #<^v^^>#
                ######.#
            """.trimIndent()
            expectThat(fastestPathThroughBlizzard(input).first.minute)
                .isEqualTo(18)
        }

        @Test
        internal fun `part 1`() {
            expectThat(fastestPathThroughBlizzard(input).first.minute)
                .isEqualTo(373)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                #.######
                #>>.<^<#
                #.<..<<#
                #>v.><>#
                #<^v^^>#
                ######.#
            """.trimIndent()
            expectThat(thereAndBackAndThere(input))
                .isEqualTo(54)
        }

        @Test
        internal fun `part 2`() {
            expectThat(thereAndBackAndThere(input))
                .isEqualTo(997)
        }
    }

    private val input by lazy { readFile("/input-day24.txt")}

}
