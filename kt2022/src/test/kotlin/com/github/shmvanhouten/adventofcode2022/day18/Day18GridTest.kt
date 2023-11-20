package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.Grid3d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day18GridTest {

    @Nested
    inner class Part1 {

        @Test
        internal fun parse() {
            val input = """
                2,2,2
                1,2,2
            """.trimIndent()
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(cubes.count { it == '#' }).isEqualTo(2)
        }

        @Test
        internal fun `an single cube has 6 sides unexposed`() {
            val input = "2,2,2"
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSidesNoBubble(cubes)).isEqualTo(6)
        }

        @Test
        internal fun `since both cubes are connected they both have 5 sides exposed`() {
            val input = """
                2,2,2
                1,2,2
            """.trimIndent()
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSidesNoBubble(cubes)).isEqualTo(10)
        }

        @Test
        internal fun `if both cubes are not connected they both have 6 sides exposed`() {
            val input = """
                2,2,2
                1,1,2
            """.trimIndent()
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSidesNoBubble(cubes)).isEqualTo(12)
        }

        @Test
        internal fun example() {
            val input = """
                2,2,2
                1,2,2
                3,2,2
                2,1,2
                2,3,2
                2,2,1
                2,2,3
                2,2,4
                2,2,6
                1,2,5
                3,2,5
                2,1,5
                2,3,5
            """.trimIndent()
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSidesNoBubble(cubes)).isEqualTo(64)
        }

        @Test
        internal fun `part 1`() {
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSidesNoBubble(cubes)).isEqualTo(4628)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `because 1,1 is surrounded by cubes, the sides of those cubes are not exposed`() {
            val z0 = """
                ...
                .#.
                ...
            """.trimIndent()
            val z1 = """
                .#.
                #.#
                .#.
        """.trimIndent()
            val z2 = """
                ...
                .#.
                ...
            """.trimIndent()
            val squares = Grid3d.from3dPicture(listOf(z0, z1, z2))
            assertThat(countExposedSides(squares)).isEqualTo(6 * 5)
        }

        @Test
        internal fun `now 1,1 and 2,1 are surrounded by squares`() {
            val z0 = """
                ....
                .##.
                ....
            """.trimIndent()
            val z1 = """
                .##.
                #..#
                .##.
            """.trimIndent()
            val z2 = """
                ....
                .##.
                ....
            """.trimIndent()
            val squares = Grid3d.from3dPicture(listOf(z0, z1, z2))
            assertThat(countExposedSides(squares)).isEqualTo(8 * 4 + 2 * 5)
        }

        @Test
        fun `air pockets are ignored`() {
            val z0 = """
                .##.
                #..#
                .##.
            """.trimIndent()
            val z1 = """
                ....
                .##.
                ....
            """.trimIndent()
            val squares = Grid3d.from3dPicture(listOf(z0, z1))
            assertThat(countExposedSides(squares)).isEqualTo(4 * 5 + 2 * 6 + 2 * 5)
        }

        @Test
        internal fun example() {
            val input = """
                2,2,2
                1,2,2
                3,2,2
                2,1,2
                2,3,2
                2,2,1
                2,2,3
                2,2,4
                2,2,6
                1,2,5
                3,2,5
                2,1,5
                2,3,5
            """.trimIndent()
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSides(cubes)).isEqualTo(58)
        }

        @Test
        internal fun `part 2`() {
            val cubes = Grid3d.fromCoordinates(input, matching = '#', missing = '.')
            assertThat(countExposedSides(cubes)).isEqualTo(2582)
        }
    }

    private val input by lazy { readFile("/input-day18.txt")}

}
