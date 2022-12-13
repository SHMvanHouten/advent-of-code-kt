package com.github.shmvanhouten.adventofcode2022.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day13Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `{1} vs {2} is ordered correctly`() {
            val input = """
                [1]
                [2]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `{2} vs {1} is NOT ordered correctly`() {
            val input = """
                [2]
                [1]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isFalse
        }

        @Test
        internal fun `if left runs out of same items first, pairs are ordered correctly`() {
            val input = """
                [1]
                [1,1]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `if right runs out of same items first, pairs are NOT ordered correctly`() {
            val input = """
                [1,1]
                [1]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isFalse
        }

        @Test
        internal fun `lists may be empty`() {
            val input = """
                []
                [1]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `example pt 1, Compare {1,1,3,1,1} vs {1,1,5,1,1}`() {
            val input = """[1,1,3,1,1]
                |[1,1,5,1,1]""".trimMargin().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `list of lists`() {
            val input = """
                [[2]]
                [[1]]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isFalse
        }

        @Test
        internal fun `example pt 2`() {
            val input = """
                [[1],[2,3,4]]
                [[1],4]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `example pt 8`() {
            val input = """
                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isFalse
        }

        @Test
        internal fun `my own example`() {
            val input = """
                [1,[2,4],3]
                [1,[2,4],2]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isFalse
        }

        @Test
        internal fun example() {
            val input = """
                [1,1,3,1,1]
                [1,1,5,1,1]

                [[1],[2,3,4]]
                [[1],4]

                [9]
                [[8,7,6]]

                [[4,4],4,4]
                [[4,4],4,4,4]

                [7,7,7,7]
                [7,7,7]

                []
                [3]

                [[[]]]
                [[]]

                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
            """.trimIndent()

            val correctIndices = findIndicesOfCorrectPacketPairs(input)
            assertThat(correctIndices).isEqualTo(listOf(1, 2, 4, 6))
            assertThat(correctIndices.sum()).isEqualTo(13)
        }

        @Test
        internal fun `failing input from part 1`() {
            // [5,3],10,[],[4]
            val input = """
                [[1,0],[[[5,3],10,[],[4]]],[[],8,9],[[2],[[6,5,10]]],[[[2,10],2]]]
                [[9,[0],[],5,6],[2],[],[1,[4,[7,4,3,8,0],[1,5,1,1],[3],7],[4,10,5,[4,10]]]]
            """.trimIndent().lines()

            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `failing on {}`() {
            val input = """
                [[[[],2,[7,5,4,7,7],10],[[3],[2,8],1,6,[]],2],[[9,[3,6,5],[9,10,1,6]]],[],[[[3,10,5,6,0],[],[4,4,9,6,6],1,[8,4,7,1,0]]]]
                [[1,[2,5,[10,6]],0,10],[[0,3]]]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0], input[1])).isTrue
        }

        @Test
        internal fun `reverse of example p7`() {
            val input = """
                [[]]
                [[[]]]
            """.trimIndent().lines()
            assertThat(orderedCorrectly(input[0],input[1])).isTrue
        }

        @Test
        internal fun `part 1`() {
            assertThat(findIndicesOfCorrectPacketPairs(input).sum()).isEqualTo(6070)
            // 4820 too low
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val input = """
                [1,1,3,1,1]
                [1,1,5,1,1]

                [[1],[2,3,4]]
                [[1],4]

                [9]
                [[8,7,6]]

                [[4,4],4,4]
                [[4,4],4,4,4]

                [7,7,7,7]
                [7,7,7]

                []
                [3]

                [[[]]]
                [[]]

                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
            """.trimIndent()
            val sorted = sortedWithDividers(input)
            assertThat(sorted).isEqualTo(
                """
                    []
                    [[]]
                    [[[]]]
                    [1,1,3,1,1]
                    [1,1,5,1,1]
                    [[1],[2,3,4]]
                    [1,[2,[3,[4,[5,6,0]]]],8,9]
                    [1,[2,[3,[4,[5,6,7]]]],8,9]
                    [[1],4]
                    [[2]]
                    [3]
                    [[4,4],4,4]
                    [[4,4],4,4,4]
                    [[6]]
                    [7,7,7]
                    [7,7,7,7]
                    [[8,7,6]]
                    [9]
                """.trimIndent().lines()
            )
            assertThat(sorted.decoderKey()).isEqualTo(140)
        }

        @Test
        internal fun `part 2`() {
            val sorted = sortedWithDividers(input)
//            sorted.forEach { println(it) }
            assertThat(sorted.decoderKey()).isLessThan(21978)
            assertThat(sorted.decoderKey()).isGreaterThan(20160)
        }
    }

    private val input by lazy { readFile("/input-day13.txt")}

}
