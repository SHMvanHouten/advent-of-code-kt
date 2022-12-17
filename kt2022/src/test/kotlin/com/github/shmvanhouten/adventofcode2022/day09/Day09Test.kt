package com.github.shmvanhouten.adventofcode2022.day09

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day09Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `head and tail start in the same place, given head moves 1 the tail stays in the same place`() {
            val input = "R 1"

            assertThat(LongRopeBridge().follow(input).countPlacesTailVisited())
                .isEqualTo(1)
        }

        @Test
        internal fun `given head moves twice in the same direction from the start the tail moves once in that same direction`() {
            val input = "R 2"

            assertThat(LongRopeBridge().follow(input).countPlacesTailVisited())
                .isEqualTo(2)
        }

        @Test
        internal fun `given head moves twice right, but then up two, the tail skips one step of the head`() {
            val input = """
                R 2
                U 2
            """.trimIndent()

            assertThat(LongRopeBridge().follow(input).countPlacesTailVisited())
                .isEqualTo(3)
        }

        @Test
        internal fun example() {
            val input = """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
            """.trimIndent()

            val bridge = LongRopeBridge().follow(input)
            println(draw(bridge.placesVisitedByTail, '#'))

            assertThat(bridge.countPlacesTailVisited())
                .isEqualTo(13)
        }

        @Test
        internal fun `part 1`() {
            val bridge = LongRopeBridge().follow(input)

            assertThat(bridge.countPlacesTailVisited()).isEqualTo(6357)
        }

    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                R 5
                U 8
                L 8
                D 3
                R 17
                D 10
                L 25
                U 20
            """.trimIndent()
            val bridge = LongRopeBridge(10).follow(input)

            assertThat(bridge.countPlacesTailVisited())
                .isEqualTo(36)

            assertThat(draw(bridge.placesVisitedByTail, '#', '.').replace(' ', '.'))
                .isEqualTo(
                    """
                        #.....................
                        #.............###.....
                        #............#...#....
                        .#..........#.....#...
                        ..#..........#.....#..
                        ...#........#.......#.
                        ....#......#.........#
                        .....#..............#.
                        ......#............#..
                        .......#..........#...
                        ........#........#....
                        .........########.....
                    """.trimIndent()
                )
        }

        @Test
        internal fun `part 2`() {
            val bridge = LongRopeBridge(10).follow(input)
            println(draw(bridge.placesVisitedByTail, '#').replace(' ', '.'))

            assertThat(bridge.countPlacesTailVisited())
                .isEqualTo(2627)
        }
    }

    private val input by lazy { readFile("/input-day09.txt")}

}
