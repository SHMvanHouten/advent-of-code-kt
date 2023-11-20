package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.lambda.identity
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class GridTest {
    private val grid: Grid<Int>
        get() {
            return intGridFromSpaceDelimitedString(input)
        }

    @Test
    fun `create a grid from input`() {
        val grid: Grid<Int> = Grid(input) {row -> row.split(' ').map { it.toInt() }}
        expectThat(grid[2, 2])
            .isEqualTo(31)
        expectThat(grid.width)
            .isEqualTo(20)
        expectThat(grid.height)
            .isEqualTo(20)
    }

    @Test
    internal fun `bool grid from coordinates`() {
        val input = """
                2,2
                1,2
            """.trimIndent()
        val grid = boolGridFromCoordinates(input)
        expectThat(grid.count{ it } ).isEqualTo(2)
        expectThat( grid.firstCoordinateMatching { it } ).isEqualTo(Coordinate(1,2))
    }

    @Test
    fun `bool grid from picture`() {
        val input = """
            .#.
            #.#
            .#.
        """.trimIndent()
        val grid = boolGridFromPicture(input, '#')

        expect {
            that(grid.count{ it } ).isEqualTo(4)
            that(grid.coordinatesMatching(::identity)).isEqualTo(listOf(
                Coordinate(1, 0),
                Coordinate(0, 1),
                Coordinate(2, 1),
                Coordinate(1, 2)
            ))
        }
    }

    @Test
    fun `foreach indexed`() {
        val mutableList = mutableListOf<String>()

        grid.forEachIndexed { coord, element -> mutableList.add("${coord.x},${coord.y},$element") }

        expectThat(mutableList)
            .hasSize(400)
        expectThat(mutableList.first())
            .isEqualTo("0,0,8")
        expectThat(mutableList.last())
            .isEqualTo("19,19,48")
    }

    @Test
    fun `get row`() {
        expectThat(grid[2])
            .isEqualTo(listOf(81,49,31,73,55,79,14,29,93,71,40,67,53,88,30,3,49,13,36,65))
    }

    @Test
    fun `get column`() {
        expectThat(grid.getColumn(5))
            .isEqualTo(listOf(15,81,79,60,67,3,23,62,73,0,75,35,71,94,35,62,25,30,31,51))
    }

    @Test
    fun `get horizontal line`() {
        expectThat(grid.horizontalLineFrom(Coordinate(2, 2), length = 4))
            .isEqualTo(listOf(31, 73, 55, 79))
    }

    @Test
    fun `get vertical line`() {
        expectThat(grid.verticalLineFrom(Coordinate(2,2), length = 4))
            .isEqualTo(listOf(31, 95, 16, 32))
    }

    @Test
    fun getDiagonalLine() {
        expectThat(grid.diagonalLineFrom(Coordinate(3,3), length = 4))
            .isEqualTo(listOf(23, 51, 3, 67))
    }

    @Test
    fun `get diagonal line reverse`() {
        expectThat(grid.revDiagonalLineFrom(Coordinate(4,0), length = 3))
            .isEqualTo(listOf(38, 40, 31))
    }

    @Test
    fun `windowed horizontal`() {
        val windows = grid.windowedHorizontal(4)
        expectThat(windows[16])
            .isEqualTo(listOf(50, 77, 91, 8))
        expectThat(windows[17])
            .isEqualTo(listOf(49, 49, 99, 40))
        expectThat(windows.size)
            .isEqualTo(((grid.width - 3) * grid.height))
        expectThat(windows.last())
            .isEqualTo(listOf(89, 19, 67, 48))
    }

    @Test
    fun `windowed vertical`() {
        val windows = grid.windowedVertical(3)
        expectThat(windows[17])
            .isEqualTo(listOf(20, 20, 1))
        expectThat(windows[18])
            .isEqualTo(listOf(2, 49, 49))
        expectThat(windows.size)
            .isEqualTo(((grid.height - 2) * grid.width))
        expectThat(windows.last())
            .isEqualTo(listOf(16, 54, 48))
    }

    @Test
    fun `windowed diagonal`() {
        val windows = grid.windowedDiagonal(5)
        expectThat(windows[15])
            .isEqualTo(listOf(12, 4, 13, 36, 80))
        expectThat(windows[16])
            .isEqualTo(listOf(49, 49, 95, 71, 99))
        expectThat(windows.last())
            .isEqualTo(listOf(32, 40, 4, 5, 48))
    }

    @Test
    fun `tiny grid diagonal windowed`() {
        val tinyGrid = """
            1 2 3 4
            5 6 7 8
            9 10 11 12
            13 14 15 16
        """.trimIndent()
        val grid = intGridFromSpaceDelimitedString(tinyGrid)
        val windows = grid.windowedDiagonal(2)
        expectThat(windows.size)
            .isEqualTo(3 * 3)
    }

    @Test
    fun `tiny grid diagonal windowed reverse`() {
        val tinyGrid = """
            1 2 3 4
            5 6 7 8
            9 10 11 12
            13 14 15 16
        """.trimIndent()
        val grid = intGridFromSpaceDelimitedString(tinyGrid)
        val windows = grid.windowedDiagonalReverse(2)
        expectThat(windows.size)
            .isEqualTo(3 * 3)

        expectThat(windows.first())
            .isEqualTo(listOf(2, 5))
        expectThat(windows.last())
            .isEqualTo(listOf(12, 15))
    }

    @Nested
    inner class Print {

        @Test
        fun `we can print the int grid`() {
            // GIVEN
            val tinyGrid = """
            1 2 3 4
            5 6 7 8
            9 10 11 12
            13 14 15 16
        """.trimIndent()

            val grid = intGridFromSpaceDelimitedString(tinyGrid)

            // WHEN
            val result = grid.toString(" ")

            // THEN
            expectThat(result).isEqualTo(tinyGrid)
        }

        @Test
        fun `we can print evenly spaced`() {
            // GIVEN
            val tinyGrid = """
            1 2 3 4
            5 6 7 8
            9 10 11 12
            13 14 15 16
        """.trimIndent()

            val grid = intGridFromSpaceDelimitedString(tinyGrid)

            // WHEN
            val result = grid.toStringEvenlySpaced()

            // THEN
            val expected = """
                1  2  3  4
                5  6  7  8
                9  10 11 12
                13 14 15 16
            """.trimIndent()
            expectThat(result).isEqualTo(expected)
        }
    }
}

private val input = """
    08 02 22 97 38 15 00 40 00 75 04 05 07 78 52 12 50 77 91 08
    49 49 99 40 17 81 18 57 60 87 17 40 98 43 69 48 04 56 62 00
    81 49 31 73 55 79 14 29 93 71 40 67 53 88 30 03 49 13 36 65
    52 70 95 23 04 60 11 42 69 24 68 56 01 32 56 71 37 02 36 91
    22 31 16 71 51 67 63 89 41 92 36 54 22 40 40 28 66 33 13 80
    24 47 32 60 99 03 45 02 44 75 33 53 78 36 84 20 35 17 12 50
    32 98 81 28 64 23 67 10 26 38 40 67 59 54 70 66 18 38 64 70
    67 26 20 68 02 62 12 20 95 63 94 39 63 08 40 91 66 49 94 21
    24 55 58 05 66 73 99 26 97 17 78 78 96 83 14 88 34 89 63 72
    21 36 23 09 75 00 76 44 20 45 35 14 00 61 33 97 34 31 33 95
    78 17 53 28 22 75 31 67 15 94 03 80 04 62 16 14 09 53 56 92
    16 39 05 42 96 35 31 47 55 58 88 24 00 17 54 24 36 29 85 57
    86 56 00 48 35 71 89 07 05 44 44 37 44 60 21 58 51 54 17 58
    19 80 81 68 05 94 47 69 28 73 92 13 86 52 17 77 04 89 55 40
    04 52 08 83 97 35 99 16 07 97 57 32 16 26 26 79 33 27 98 66
    88 36 68 87 57 62 20 72 03 46 33 67 46 55 12 32 63 93 53 69
    04 42 16 73 38 25 39 11 24 94 72 18 08 46 29 32 40 62 76 36
    20 69 36 41 72 30 23 88 34 62 99 69 82 67 59 85 74 04 36 16
    20 73 35 29 78 31 90 01 74 31 49 71 48 86 81 16 23 57 05 54
    01 70 54 71 83 51 54 69 16 92 33 48 61 43 52 01 89 19 67 48
""".trimIndent()