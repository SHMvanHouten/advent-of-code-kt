package com.github.shmvanhouten.adventofcode2024.day15

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day15Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val example = """
                ########
                #..O.O.#
                ##@.O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########

                <^^>>>vv<v>>v<<
            """.trimIndent()
            val (grid, instructions) = parse(example)
            val afterInstruction = followInstructions(grid, instructions)
            expectThat(afterInstruction.toString())
                .isEqualTo("""
                    ########
                    #....OO#
                    ##.....#
                    #.....O#
                    #.#O@..#
                    #...O..#
                    #...O..#
                    ########
                """.trimIndent())
            expectThat(getGPS(afterInstruction)).isEqualTo(2028)
        }

        @Test
        internal fun `example 2`() {
            val (grid, instructions) = parse(example)
            val afterInstruction = followInstructions(grid, instructions)
            expectThat(afterInstruction.toString())
                .isEqualTo("""
                    ##########
                    #.O.O.OOO#
                    #........#
                    #OO......#
                    #OO@.....#
                    #O#.....O#
                    #O.....OO#
                    #O.....OO#
                    #OO....OO#
                    ##########
                """.trimIndent())
            expectThat(getGPS(afterInstruction)).isEqualTo(10092)
        }

        @Test
        internal fun `part 1`() {
            val (grid, instructions) = parse(input)
            val afterInstruction = followInstructions(grid, instructions)
            expectThat(getGPS(afterInstruction)).isEqualTo(1526018)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            expectThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            expectThat(1).isEqualTo(1)
        }
    }

    private val input by lazy { readFile("/input-day15.txt")}
    private val example = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent()
}
