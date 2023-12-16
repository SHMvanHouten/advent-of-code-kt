package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.*

class Grid3dTest {
    private val grid: Grid3d<Char>
        get() {
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
            return Grid3d.from3dPicture(listOf(z0, z1, z2))
        }

    @Test
    fun `from 3d picture`() {
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
        val grid3d = Grid3d.from3dPicture(listOf(z0, z1, z2))

        expect {
            that(grid3d[0].toString()).isEqualTo(z0)
            that(grid3d[1].toString()).isEqualTo(z1)
            that(grid3d[2].toString()).isEqualTo(z2)
        }
    }

    @Test
    fun `basic getting functions`() {
        expect {
            that(grid[Coordinate3d(1, 1, 0)]).isEqualTo('#')

            that(grid[0][1][2]).isEqualTo('.')

            that(grid.getOrNull(Coordinate3d(1,2,5))).isNull()
            that(grid.getOrNull(Coordinate3d(5,1,1))).isNull()
            that(grid.getOrNull(Coordinate3d(2,1,1))).isEqualTo('#')

            that(grid.hasElementAt(Coordinate3d(1, 1, 3))).isFalse()
            that(grid.hasElementAt(Coordinate3d(2,2,2))).isTrue()
            that(grid.hasElementAt(Coordinate3d(3,1,1))).isFalse()
        }
    }

    @Test
    fun perimeter() {
        val grid0 = """
            #########
            #########
            #########
            #########
            #########
        """.trimIndent()
        val grid1 = """
            #########
            #.......#
            #.......#
            #.......#
            #########
        """.trimIndent()
        val grid2 = """
            #########
            #.......#
            #.......#
            #.......#
            #########
        """.trimIndent()
        val grid3 = """
            #########
            #.......#
            #.......#
            #.......#
            #########
        """.trimIndent()
        val grid4 = """
            #########
            #########
            #########
            #########
            #########
        """.trimIndent()

        val grid3d = Grid3d.from3dPicture(listOf(grid0, grid1, grid2, grid3, grid4)).withIndex()

        val perimeter = grid3d.perimeter().toList()
        expect {
            that(perimeter).hasSize(2 * 3 * (9 + 5 - 2) + 2 * (9 * 5))
            that(perimeter.map { it.item }).doesNotContain('.')
            that(perimeter.map { it.item }.count { it == '#' }).isEqualTo(162)

            that(perimeter.map { it.location }.filter { it.z == 0 || it.z == 4 }).hasSize(2 * 9 * 5)
            that(perimeter.map { it.location }.filter { it.x == 0 || it.x == 8 }).hasSize(2 * 5 * 5)
            that(perimeter.map { it.location }.filter { it.y == 0 || it.y == 4 }).hasSize(2 * 9 * 5)
        }
    }
}