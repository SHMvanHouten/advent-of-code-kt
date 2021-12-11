package com.github.shmvanhouten.adventofcode2021.day11

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day11Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `each octopus increases by 1 each tick`() {
            val input = """111
                |111
                |111
            """.trimMargin()
            val octopusGrid = OctopusGrid(input)
            assertThat(
                octopusGrid.tick().drawGrid(),
                equalTo(
                    """222
                        |222
                        |222
                    """.trimMargin()
                )
            )
            assertThat(
                OctopusGrid(input).tick(2).drawGrid(),
                equalTo(
                    """333
                        |333
                        |333
                    """.trimMargin()
                )
            )
        }

        @Test
        internal fun `if an octopus flashes it goes to 0 energy and all octopuses around it get an extra energy`() {
            val input = """111
                |191
                |111
            """.trimMargin()
            val grid = OctopusGrid(input)
            assertThat(
                grid.tick().drawGrid(),
                equalTo(
                    """333
                        |303
                        |333
                    """.trimMargin()
                )
            )
        }

        @Test
        internal fun `example 1`() {
            assertThat(
                OctopusGrid(exampleInput).tick().drawGrid(),
                equalTo("""6594254334
3856965822
6375667284
7252447257
7468496589
5278635756
3287952832
7993992245
5957959665
6394862637""")
            )
            assertThat(
                OctopusGrid(exampleInput).tick(10).drawGrid(),
                equalTo("""0481112976
0031112009
0041112504
0081111406
0099111306
0093511233
0442361130
5532252350
0532250600
0032240000""")
            )
        }

        @Test
        internal fun `example 1 100 ticks`() {
            val after100Ticks = OctopusGrid(exampleInput).tick(100)
            assertThat(
                after100Ticks.drawGrid(),
                equalTo("""0397666866
0749766918
0053976933
0004297822
0004229892
0053222877
0532222966
9322228966
7922286866
6789998766""")
            )
            assertThat(after100Ticks.flashes, equalTo(1656))
        }

        @Test
        internal fun `part 1`() {
            val after100Ticks = OctopusGrid(input).tick(100)
            assertThat(after100Ticks.flashes, equalTo(1725))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            assertThat(
                findFirstTimeAllOctopusesFlash(OctopusGrid(exampleInput)),
                equalTo(195)
            )

        }

        @Test
        internal fun `part 2`() {
            assertThat(
                findFirstTimeAllOctopusesFlash(OctopusGrid(input)),
                equalTo(308)
            )
        }
    }

    private val input by lazy { readFile("/input-day11.txt") }
    private val exampleInput = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""

}
