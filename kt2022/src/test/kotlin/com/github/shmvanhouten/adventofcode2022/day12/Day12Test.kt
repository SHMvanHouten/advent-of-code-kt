package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day12Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val example = """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent()
            val shortestPath = shortestPath(example)
            assertThat(shortestPath.size - 1).isEqualTo(31)
        }

        @Test
        internal fun `part 1`() {
            val shortestPath = shortestPath(input)
            assertThat(shortestPath.size - 1).isEqualTo(504)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val example = """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent()
            val shortestPath = shortestPathFromAnyATile(example)
            assertThat(shortestPath.size - 1).isEqualTo(29)
        }

        @Test
        internal fun `part 2`() {
            val shortestPath = shortestPathFromAnyATile(input)
            assertThat(shortestPath.size - 1).isEqualTo(500)
        }
    }

    private val input by lazy { readFile("/input-day12.txt")}

}
