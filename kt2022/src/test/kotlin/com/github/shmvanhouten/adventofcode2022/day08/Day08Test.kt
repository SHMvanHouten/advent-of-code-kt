package com.github.shmvanhouten.adventofcode2022.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day08Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun bla() {
//            forEachOrientationPrint(input)
            forEachOrientationPrint(
                """
                abc
                123
                *&^
            """.trimIndent()
            )
        }

        @Test
        internal fun example() {
            val example = """
                |30373
                |25512
                |65332
                |33549
                |35390
            """.trimMargin()

            assertThat(treesVisible(example)).hasSize(21)
        }

        @Test
        @Disabled("intellij is annoying again")
        internal fun `part 1`() {
            assertThat(treesVisible(input)).hasSize(1736)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val example = """
                |30373
                |25512
                |65332
                |33549
                |35390
            """.trimMargin()
            assertThat(
                findBestTree(example.toCoordinateMap { c, coord -> c.digitToInt() })
            ).isEqualTo(8)
        }

        @Test
        internal fun `part 2`() {
            assertThat(
                findBestTree(input.toCoordinateMap { c, coord -> c.digitToInt() })
            ).isEqualTo(268800)
        }
    }

    private val input by lazy { readFile("/input-day08.txt") }

}
