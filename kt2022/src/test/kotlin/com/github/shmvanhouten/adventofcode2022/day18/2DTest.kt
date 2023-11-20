package com.github.shmvanhouten.adventofcode2022.day18

import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `2DTest` {

    @Test
    internal fun parse() {
        val input = """
                2,2
                1,2
            """.trimIndent()
        val squares = parse2d(input)
        assertThat(squares.count{ it == DROPLET } ).isEqualTo(2)
    }

    @Test
    internal fun `an single square has 4 sides unexposed`() {
        val input = "2,2"
        val squares = parse2d(input)
        assertThat(countExposedSides(squares)).isEqualTo(4)
    }

    @Test
    internal fun `since both squares are connected they both have 3 sides exposed`() {
        val input = """
                2,2
                1,2
            """.trimIndent()
        val squares = parse2d(input)
        assertThat(countExposedSides(squares)).isEqualTo(6)
    }

    @Test
    internal fun `if both squares are not connected they both have 4 sides exposed`() {
        val input = """
                2,2
                1,1
            """.trimIndent()
        val squares = parse2d(input)
        assertThat(countExposedSides(squares)).isEqualTo(8)
    }

    @Test
    internal fun `because 1,1 is surrounded by squares, the sides of those squares are not exposed`() {
        val input = """
            .#.
            #.#
            .#.
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(16 - 4)
    }

    @Test
    internal fun `now 1,1 and 2,1 are surrounded by squares`() {
        val input = """
            .##.
            #..#
            .##.
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(14) //24 - 4 - 6
    }

    @Test
    internal fun `now 1,1 and 2,1 and 1,2 are surrounded by squares`() {
        val input = """
            .##.
            #..#
            #.#.
            .#..
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(16)
    }

    @Test
    internal fun `now the air bubble is open`() {
        val input = """
            .##.
            #..#
            #.#.
            ..#.
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(22)
    }

    @Test
    fun `big ol bubble`() {
        val input = """
            .########
            .#......#
            .#.####.#
            ..#.....#
            ..#.####.
            ....#....
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(56) //32 + 24
    }

    @Test
    internal fun `more bends`() {
        val input = """
            .##.
            #..#
            #.#.
            ..#.
            .#..
        """.trimIndent()
        val squares = gridFromPicture(input)
        assertThat(countExposedSides(squares)).isEqualTo(26)
    }

    private fun gridFromPicture(input: String): Grid<Char> {
        return Grid(input) { s ->
            s.map { c -> if(c == '#') DROPLET else UNVISITED }
        }
    }
}