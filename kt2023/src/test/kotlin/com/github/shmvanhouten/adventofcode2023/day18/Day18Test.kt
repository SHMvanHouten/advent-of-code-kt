package com.github.shmvanhouten.adventofcode2023.day18

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day18Test {

    val example = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        fun `simple square`() {
            val example = """
                R 4
                D 4
                L 4
                U 4
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25)
        }

        //  ..#####
        //  ..#...#
        //  ###...#
        //  #.....#
        //  #######
        @Test
        fun `square with an attachment`() {
            val example = """
                R 4
                D 4
                L 6
                U 2
                R 2
                U 2
            """.trimIndent()
            println(toGrid(example))
            expectThat(dig(example)).isEqualTo(25 + 6)
        }

        //  #####..
        //  #...#..
        //  #...###
        //  #.....#
        //  #.....#
        //  #...###
        //  #...#..
        //  #####..
        @Test
        fun `square with an attachment on the right`() {
            val example = """
                R 4
                D 2
                R 2
                D 3
                L 2
                D 2
                L 4
                U 7
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(40 + 8)
        }

        //  #####
        //  #...#
        //  #...#
        //  ##..#
        //  .####
        @Test
        fun `square with a piece missing`() {
            val example = """
                R 4
                D 4
                L 3
                U 1
                L 1
                U 3
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25 - 1)
        }

        //  #####
        //  #...#
        //  ###.#
        //  ..#.#
        //  ..###
        @Test
        fun `square with a bigger piece missing`() {
            val example = """
                R 4
                D 4
                L 2
                U 2
                L 2
                U 2
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25 - 4)
        }

        //  ###..
        //  #.#..
        //  #.###
        //  #...#
        //  #####
        @Test
        fun `square with a piece missing on the other side`() {
            val example = """
                R 2
                D 2
                R 2
                D 2
                L 4
                U 4
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25 - 4)
        }

        //  #####
        //  #...#
        //  #.###
        //  #.#..
        //  ###..
        @Test
        fun `square with a piece missing on the bottom right`() {
            val example = """
                R 2
                D 2
                R 2
                D 2
                L 4
                U 4
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25 - 4)
        }

        //  ...####
        //  ...#..#
        //  ...#..#
        //  ####..#
        //  #.....#
        //  #.....#
        //  #######
        @Test
        fun `square with a piece missing on the top left`() {
            val example = """
                L 3
                D 3
                L 3
                D 3
                R 6
                U 6
            """.trimIndent()
            println(toGrid(example))
            expectThat(dig(example)).isEqualTo(49 - 9)
        }

        //  ########
        //  #......#..
        //  #.####.#
        //  #.#..#.#..
        //  ###..###..
        @Test
        fun `a part taken out`() {
            val example = """
                R 7
                D 4
                L 2
                U 2
                L 3
                D 2
                L 2
                U 4
            """.trimIndent()
            println(toGrid(example))
            expectThat(dig(example)).isEqualTo((8 * 5) - 4)
        }

        @Test
        fun `polygon with a negative attachment`() {
            val example = """
                R 4
                U 4
                L 5
                D 1
                R 1
                D 3
            """.trimIndent()
            expectThat(dig(example)).isEqualTo(25 + 2)
        }

        //  ########
        //  #......#..
        //  #.####.#
        //  #.#..#.#..
        //  #.#..#.#..
        //  #.#..###..
        //  #.#.......
        //  #.######..
        //  #......#..
        //  ########..
        @Test
        fun `polygon with a bigger attachment`() {
            val example = """
                R 7
                D 5
                L 2
                U 3
                L 3
                D 5
                R 5
                D 2
                L 7
                U 9
            """.trimIndent()

            println(toGrid(example))
            expectThat(dig(example)).isEqualTo((8 * 10) - 11)
        }


        @Test
        internal fun `example 1`() {
            println(toGrid(example))
            expectThat(dig(example)).isEqualTo(62)
        }

        @Test
        internal fun `part 1`() {
//            println(toGrid(input))
            expectThat(dig(input)).isEqualTo(68115)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(digHex(example)).isEqualTo(952408144115)
        }

        @Test
        internal fun `part 2`() {
            // ballpark 71262483231402
            expectThat(digHex(input)).isEqualTo(71262565063800)
        }
    }

    private val input by lazy { readFile("/input-day18.txt")}

}
