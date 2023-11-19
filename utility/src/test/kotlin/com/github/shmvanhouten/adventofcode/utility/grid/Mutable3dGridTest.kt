package com.github.shmvanhouten.adventofcode.utility.grid

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.isEqualTo

class Mutable3dGridTest {
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
    fun `mutable grid can be changed`() {
        val mutableGrid = grid.toMutable3dGrid()

        mutableGrid[Coordinate3d(1,1,2)] = 'G'
        mutableGrid[2,2,2] = '@'
        mutableGrid[2][1][2] = '~'
        mutableGrid[0][0,0] = ']'

        expect {
            that(mutableGrid[Coordinate3d(1, 1, 2)])
                .isEqualTo('G')
            that(mutableGrid[2, 2, 2])
                .isEqualTo('@')
            that(mutableGrid[2][1][2])
                .isEqualTo('~')
            that(mutableGrid[Coordinate3d(0, 0, 0)])
                .isEqualTo(']')
        }
    }
}