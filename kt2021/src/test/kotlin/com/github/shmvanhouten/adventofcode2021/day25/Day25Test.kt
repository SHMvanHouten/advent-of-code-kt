package com.github.shmvanhouten.adventofcode2021.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day25Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `parse the input into a collection of downward facing and right facing sea cucumbers`() {
            val input = """
                v..>>
                vv..>
                >v>..
            """.trimIndent()
            val (eastFacing, southFacing) = toSeaCucumbers(input)
            assertThat(southFacing, hasSize(equalTo(4)))
            assertThat(eastFacing, hasSize(equalTo(5)))
            assertThat(southFacing,
                equalTo(setOf(
                    Coordinate(0,0),
                    Coordinate(0,1),
                    Coordinate(1,1),
                    Coordinate(1,2)
                )))
        }

        @Test
        internal fun example() {
            val example = """
                v...>>.vv>
                .vv>>.vv..
                >>.>v>...v
                >>v>>.>.v.
                v>v.vv.v..
                >.>>..v...
                .vv..>.>v.
                v.v..>>v.v
                ....v..v.>
            """.trimIndent()
            val field = toSeaCucumbers(example)
            assertThat(
                countStepsUntilMigrationHasStopped(field),
                equalTo(58)
            )
        }

        @Test
        internal fun `part 1`() {
            val field = toSeaCucumbers(input)
            assertThat(
                countStepsUntilMigrationHasStopped(field),
                equalTo(532)
            )
        }
    }

    private val input by lazy { readFile("/input-day25.txt")}

}
