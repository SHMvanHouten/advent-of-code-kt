package com.github.shmvanhouten.adventofcode2023.day05

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day05Test {

    val example = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

    @Nested
    inner class Part1 {

        @ParameterizedTest
        @CsvSource(
            value = [
                "79, 81",
                "14, 14",
                "55, 57",
                "13, 13",
                "16, 1"
            ]
        )
        fun `seed number corresponds to soil number`(seed: Long, soil: Long) {
            val seeds = "seeds: $seed\n\n"
            val instructions = """
                seed-to-soil map:
                50 98 2
                52 50 48
                0 15 37
            """.trimIndent()
            val almanac = toAlmanac(blocks(seeds + instructions))

            expectThat(almanac.maps.first().applyTo(seed))
                .isEqualTo(soil)
        }

        @Test
        fun `seed to soil`() {
            val almanac = toAlmanac(blocks(example))
            expectThat(almanac.listLocationNrs())
                .isEqualTo(listOf(82, 43, 86, 35))
        }

        @Test
        internal fun `example 1`() {
            val almanac = toAlmanac(blocks(example))
            expectThat(almanac.minLocation())
                .isEqualTo(35)
        }

        @Test
        internal fun `part 1`() {
            val almanac = toAlmanac(blocks(input))
            expectThat(almanac.minLocation())
                .isEqualTo(484023871)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            val almanac = toAlmanacWithSeedRanges(blocks(example))
            expectThat(almanac.minLocation())
                .isEqualTo(46)
        }

        @Test
        internal fun `part 2`() {
            val almanac = toAlmanacWithSeedRanges(blocks(input))
            expectThat(almanac.minLocation())
                .isEqualTo(46294175)
        }
    }

    private val input by lazy { readFile("/input-day05.txt")}

}
