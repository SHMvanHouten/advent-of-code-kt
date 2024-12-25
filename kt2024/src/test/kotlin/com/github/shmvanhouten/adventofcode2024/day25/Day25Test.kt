package com.github.shmvanhouten.adventofcode2024.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day25Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            expectThat(countFitting(example)).isEqualTo(3)
        }

        @Test
        internal fun `part 1`() {
            expectThat(countFitting(input)).isEqualTo(3057)
        }
    }

    private val input by lazy { readFile("/input-day25.txt")}
    private val example = """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....

        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....

        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####

        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####

        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        #####
    """.trimIndent()

}
