package com.github.shmvanhouten.adventofcode2024.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day12Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val plots = calculatePlots(charGrid(example))
            expectThat(plots.prices().sum()).isEqualTo(1930L)
        }

        @Test
        internal fun `part 1`() {
            val plots = calculatePlots(charGrid(input))
            expectThat(plots.prices().sum()).isEqualTo(1402544L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(calculatePlots(charGrid(example)).discountPrices().sum()).isEqualTo(1206L)
        }

        @Test
        fun `e example`() {
            val example = """
                EEEEE
                EXXXX
                EEEEE
                EXXXX
                EEEEE
            """.trimIndent()
            val plots = calculatePlots(charGrid(example))
            expectThat(plots.discountPrices().sum()).isEqualTo(236L)
        }

        @Test
        internal fun `part 2`() {
            expectThat(calculatePlots(charGrid(input)).discountPrices().sum()).isEqualTo(862486L)
        }
    }

    private val input by lazy { readFile("/input-day12.txt")}
    private val example = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()
}
