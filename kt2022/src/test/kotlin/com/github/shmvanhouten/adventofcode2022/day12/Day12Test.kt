package com.github.shmvanhouten.adventofcode2022.day12

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.grid.Coord
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
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
            assertThat(shortestPath.length).isEqualTo(31)
        }

        @Test
        internal fun `part 1`() {
            val shortestPath = shortestPath(input)
            assertThat(shortestPath.length).isEqualTo(504)
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
            printOut(shortestPath, charGrid(example))
            assertThat(shortestPath.length).isEqualTo(29)
        }

        @Test
        internal fun `part 2`() {
            val shortestPath = shortestPathFromAnyATile(input)
            printOut(shortestPath, charGrid(input))
            assertThat(shortestPath.length).isEqualTo(500)
        }
    }

    private fun printOut(shortestPath: Path, map: Grid<Char>) {
        var pathBefore: Path? = shortestPath
        val fullPath = mutableSetOf<Coord>()
        while (pathBefore != null) {
            fullPath += pathBefore.current.coord
            pathBefore = pathBefore.stepsBefore
        }
        map.grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                selectColor(c)

                if (fullPath.contains(Coord(x, y))) print('X')
                else print('█')

                print("\u001b[30m")
            }
            println()
        }
    }

    private fun selectColor(c: Char?) {
        when (c) {
            'a' -> print("\u001b[37m") // white-gray
            'b' -> print("\u001b[36m") // cyan
            in 'c'..'f' -> print("\u001b[34m") // blue
            in 'g'..'l' -> print("\u001b[33m") // yellow
            in 'm'..'q' -> print("\u001b[32m") // Green
            in 'r'..'u' -> print("\u001b[31m") // red
            in 'v'..'z' -> print("\u001b[30m") // black
        }
    }

    private val input by lazy { readFile("/input-day12.txt") }

}
