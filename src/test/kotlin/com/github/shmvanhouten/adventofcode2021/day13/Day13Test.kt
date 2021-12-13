package com.github.shmvanhouten.adventofcode2021.day13

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.github.shmvanhouten.adventofcode2021.day13.FoldingLine.X
import com.github.shmvanhouten.adventofcode2021.day13.FoldingLine.Y
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day13Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1 first fold`() {
            val (coordinates, instructions) = parseFoldingInstructions(exampleInput)
            assertThat(instructions.first(), equalTo(FoldingInstruction(Y, 7)))
            val expected = """#.##..#..#.
#...#......
......#...#
#...#......
.#.#..#.###
...........
...........""".toCoordinateMap('#')
            val fold = fold(coordinates, instructions.first())
            assertThat(fold, equalTo(expected) )
            assertThat(fold.size, equalTo(17))
        }

        @Test
        internal fun `example 1 second fold`() {
            val coordinates = """#.##..#..#.
#...#......
......#...#
#...#......
.#.#..#.###
...........
...........""".toCoordinateMap('#')

            val expected = """#####
#...#
#...#
#...#
#####
.....
.....""".toCoordinateMap('#')
            assertThat(
                draw(fold(coordinates, FoldingInstruction(X, 5))),
                equalTo(draw(expected))
            )
        }

        @Test
        internal fun `part 1`() {
            val (coordinates, instructions) = parseFoldingInstructions(input)
            val fold = fold(coordinates, instructions.first())
            assertThat(fold.size, equalTo(814))

        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            val (coordinates, instructions) = parseFoldingInstructions(input)
            val folds = instructions.runningFold(coordinates) { coords, instruction ->
                fold(coords, instruction)
            }
//            folds.forEach { println(draw(it)) }
            println(draw(folds.last()))
            "PZEHRAER"
        }
    }

    private val input by lazy { readFile("/input-day13.txt")}
    private val exampleInput = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5"""

}
