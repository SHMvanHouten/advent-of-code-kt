package com.github.shmvanhouten.adventofcode.utility.coordinate

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DrawCoordinatesTest {
    @Test
    internal fun draw_a_map_using_shading() {
        println(exampleInput.toIntByCoordinateMap().mapValues { increasedShadingChars[it.value] }.draw())
    }

    @Nested
    inner class To_coordinate_map {
        @Test
        internal fun `finds the coordinates for the char provided`() {
            val coordinates = """
                #.#
                ...
                .#.
            """.trimIndent().toCoordinateMap('#')
            println(draw(coordinates))
            assertThat(
                coordinates,
                equalTo(setOf(Coordinate(0, 0), Coordinate(2, 0), Coordinate(1, 2)))
            )
        }
    }

    private val exampleInput = """2199943210
3987894921
9856789892
8767896789
9899965678"""
}
