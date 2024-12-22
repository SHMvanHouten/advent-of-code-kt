package com.github.shmvanhouten.adventofcode2024.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.grid.Grid
import com.github.shmvanhouten.adventofcode.utility.grid.charGrid
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

class Day20Test {

    @Nested
    inner class Part1 {

        @Test
        fun `quickest path through example without cheats`() {
            expectThat(quickestPath(example).length).isEqualTo(84)
        }

        @Test
        fun `there is a cheat for the example that saves 64 ps`() {
            val cheats = findCheats(example)
            expectThat(cheats.filter{it.gain == 64}.toList()).hasSize(1)
        }

        @Test
        fun `there are 44 cheats that save time for the example`() {
            val findCheats = findCheats(example).toList()
            expect {
                that(findCheats.filter { it.gain == 2 }).hasSize(14)
                that(findCheats.filter { it.gain == 4 }).hasSize(14)
                that(findCheats.filter{ it.gain == 6 }).hasSize(2)
                that(findCheats.filter{ it.gain == 8 }).hasSize(4)
                that(findCheats.filter{ it.gain == 10}).hasSize(2)
                that(findCheats.filter{ it.gain == 12}).hasSize(3)
                that(findCheats.filter{ it.gain == 20}).hasSize(1)
                that(findCheats.filter{ it.gain == 36}).hasSize(1)
                that(findCheats.filter{ it.gain == 38}).hasSize(1)
                that(findCheats.filter{ it.gain == 40}).hasSize(1)
                that(findCheats.filter{ it.gain == 64}).hasSize(1)
                that(findCheats.size).isEqualTo(44)
            }
        }

        @Test
        fun `example 1 cheats2`() {
            val cheatingPaths = findCheats3(example, 2, 1)
            expectThat(cheatingPaths).isEqualTo(44)
        }

        @Test
        internal fun `part 1`() {
            val cheats = findCheats3(input, improvementNeeded = 100)
            expectThat(cheats).isEqualTo(1445)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        fun `list all open spots within cheat length`() {
            val grid = charGrid(example)
            val openSpots = grid.listAllOpenSpotsWithIn(Coordinate(1, 3), 6, setOf(Coordinate(1,3)))
            expectThat(print(grid, openSpots)).isEqualTo(
                """
                    ###############
                    #uuu#u..#.....#
                    #u#u#u#.#.###.#
                    #S#uuu#u#.#...#
                    #######.#.#.###
                    #######.#.#...#
                    #######.#.###.#
                    ###u.E#...#...#
                    ###.#######.###
                    #u..###...#...#
                    #.#####.#.###.#
                    #.#...#.#.#...#
                    #.#.#.#.#.#.###
                    #...#...#...###
                    ###############
                """.trimIndent()
            )

        }

        @Test
        internal fun `example 1 cheats`() {
            val cheats = findCheats(example, cheatLength = 20).toList()
            println(cheats.filter { it.gain == 76 }.map { it.print(charGrid(example)) }.joinToString("\n"))
            expect {
                that(cheats.filter{it.gain == 50 }).hasSize(32)
                that(cheats.filter{it.gain == 52 }).hasSize(31)
                that(cheats.filter{it.gain == 54 }).hasSize(29)
                that(cheats.filter{it.gain == 56 }).hasSize(39)
                that(cheats.filter{it.gain == 58 }).hasSize(25)
                that(cheats.filter{it.gain == 60 }).hasSize(23)
                that(cheats.filter{it.gain == 62 }).hasSize(20)
                that(cheats.filter{it.gain == 64 }).hasSize(19)
                that(cheats.filter{it.gain == 66 }).hasSize(12)
                that(cheats.filter{it.gain == 68 }).hasSize(14)
                that(cheats.filter{it.gain == 70 }).hasSize(12)
                that(cheats.filter{it.gain == 72 }).hasSize(22)
                that(cheats.filter{it.gain == 74 }).hasSize(4)
                that(cheats.filter{it.gain == 76}).hasSize(3)
            }
        }

        @Test
        fun `example 2`() {
            val cheatingPaths = findCheats3(example, 20, 50)
            expectThat(cheatingPaths).isEqualTo(285)
        }

        @Test
        internal fun `part 2`() {
            val cheats = findCheats3(input, 20, 100)
            expectThat(cheats).isEqualTo(1008040)
        }
    }

    fun print(grid: Grid<Char>, specialCoordinates: Set<Coordinate>): String {
        val mutableGrid = grid.toMutableGrid()
        specialCoordinates.forEach { mutableGrid[it] = 'u' }
        return mutableGrid.toString()
    }

    private val input by lazy { readFile("/input-day20.txt")}
    private val example = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent()

}
