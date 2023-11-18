package com.github.shmvanhouten.adventofcode2022.day08

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode.utility.grid.Coord
import com.github.shmvanhouten.adventofcode2022.day08.grid.bestTreeScenicScore
import com.github.shmvanhouten.adventofcode2022.day08.grid.visibleTrees
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.doesNotContain
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day08Test {

    @Nested
    inner class UsingGrid {
        @Nested
        inner class Part1TreesFromTheForest {

            @Test
            fun test() {
                val example = """
                |30373
                |25512
                |65332
                |33549
                |35390
            """.trimMargin()

                val visibleTrees = visibleTrees(example)
                expect {
                    that(visibleTrees).hasSize(21)
                    that(visibleTrees.map { it.location }).doesNotContain(
                        Coord(3, 1),
                        Coord(2, 2),
                        Coord(1, 3),
                        Coord(3, 3)
                    )
                    that(visibleTrees.map { it.location }).contains(
                        Coord(1, 1),
                        Coord(2, 1),
                        Coord(1, 2),
                        Coord(3, 2),
                        Coord(2, 3)
                    )
                }
            }

            @Test
            fun `part 1`() {
                assertThat(visibleTrees(input)).hasSize(1736)
            }

        }

        @Nested
        inner class Part2BestTree {
            @Test
            internal fun example() {
                val example = """
                |30373
                |25512
                |65332
                |33549
                |35390
            """.trimMargin()
                expectThat(
                    bestTreeScenicScore(example)
                ).isEqualTo(8)
            }

            @Test
            internal fun `part 2`() {
                expectThat(
                    bestTreeScenicScore(input)
                ).isEqualTo(268800)
            }
        }
    }

    @Nested
    inner class UsingCoordinates {

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
    }

    private val input by lazy { readFile("/input-day08.txt") }

}
