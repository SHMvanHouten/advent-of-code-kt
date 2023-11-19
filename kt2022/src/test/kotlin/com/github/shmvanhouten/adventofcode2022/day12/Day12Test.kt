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
        val fullPath = retracePath(shortestPath)
        map.mapIndexed { x, y, c -> printColored(c, fullPath, x, y)}
            .also(::println)
    }

    private fun retracePath(shortestPath: Path): MutableSet<Coord> {
        var pathBefore: Path? = shortestPath
        val fullPath = mutableSetOf<Coord>()
        while (pathBefore != null) {
            fullPath += pathBefore.current.coord
            pathBefore = pathBefore.stepsBefore
        }
        return fullPath
    }

    private fun printColored(c: Char, fullPath: MutableSet<Coord>, x: Int, y: Int): String {
        val color = selectColor(c)

        val char = if (fullPath.contains(Coord(x, y))) 'X'
        else '█'

        val resetToBlack = "\u001b[30m"
        return "$color$char$resetToBlack"
    }

    private fun selectColor(c: Char?): String {
        return when (c) {
            'a' -> "\u001b[37m" // white-gray
            'b' -> "\u001b[36m" // cyan
            in 'c'..'f' -> "\u001b[34m" // blue
            in 'g'..'l' -> "\u001b[33m" // yellow
            in 'm'..'q' -> "\u001b[32m" // Green
            in 'r'..'u' -> "\u001b[31m" // red
            in 'v'..'z' -> "\u001b[30m" // black
            else -> ""
        }
    }

    private val input by lazy { readFile("/input-day12.txt") }

}
