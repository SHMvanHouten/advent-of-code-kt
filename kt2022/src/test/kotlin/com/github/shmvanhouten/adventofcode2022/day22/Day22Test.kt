package com.github.shmvanhouten.adventofcode2022.day22

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.EAST
import com.github.shmvanhouten.adventofcode.utility.coordinate.Direction.SOUTH
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day22Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `simple start`() {
            val input = """
                ...#
                .#..
                #...
                ....
                
                2R3
            """.trimIndent()
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(position).isEqualTo(Coordinate(2,3))
            assertThat(facing).isEqualTo(SOUTH)
        }


        @Test
        internal fun `hitting walls`() {
            val input = """
                ...#
                .#..
                #...
                ....
                
                3R3
            """.trimIndent()
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(position).isEqualTo(Coordinate(2,3))
        }

        @Test
        internal fun `going off the map`() {
            val input = """
                ...#
                .#..
                #...
                ....
                
                3R8
            """.trimIndent()
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(position).isEqualTo(Coordinate(2,0))
        }

        @Test
        internal fun example() {
            val input = """
                |        ...#
                |        .#..
                |        #...
                |        ....
                |...#.......#
                |........#...
                |..#....#....
                |..........#.
                |        ...#....
                |        .....#..
                |        .#......
                |        ......#.
                |
                |10R5L5R10L4R5L5
            """.trimMargin("|")
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(position).isEqualTo(Coordinate(7,5))
            assertThat(facing).isEqualTo(EAST)

            assertThat(password(position, facing)).isEqualTo(6032)
        }

        @Test
        internal fun `if the wraparound is a block, don't move there`() {
            val input = """
                ...#
                .#..
                #...
                ....
                
                3R2L3
            """.trimIndent()
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(position).isEqualTo(Coordinate(3,2))
        }

        @Test
        internal fun `part 1`() {
            val board = Board(input)
            val (position, facing) = board.followInstructions()
            assertThat(password(position, facing)).isEqualTo(27436)
            //115396 too high
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example wrapped`() {
            val input = """
                |        ...#
                |        .#..
                |        #...
                |        ....
                |...#.......#
                |........#...
                |..#....#....
                |..........#.
                |        ...#....
                |        .....#..
                |        .#......
                |        ......#.
                |
                |10R5L5R10L4R5L5
            """.trimMargin("|")
            val board = Board(input, true)

            val (position, facing) = board.followInstructions()
            assertThat(password(position, facing)).isEqualTo(5031)
        }

        @Test
        internal fun scribblings() {
            /*
x = 50
  |
  v
  #### <- x = 150, y = 0
  ####
  ##   <- y = 50
  ##
####   <- y = 100
####
##
##
             */
            val a = (Coordinate(50,0)..Coordinate(100,0)).zip(Coordinate(0,150)..Coordinate(0,200)) // RIGHT, LEFT
            val b = (Coordinate(100,0)..Coordinate(150,0)).zip(Coordinate(0,200)..Coordinate(50,200)) // SAME DIR
            val c = (Coordinate(150,0)..Coordinate(150,50)).zip(Coordinate(50,150)..Coordinate(100,150)) // REVERSE
            val d = (Coordinate(100,50)..Coordinate(150,50)).zip(Coordinate(100,50)..Coordinate(100,100)) // RIGHT,LEFT
            val g = (Coordinate(50,150)..Coordinate(100,150)).zip((Coordinate(50,150)..Coordinate(50,200))) // RIGHT< LEFT
            val k = (Coordinate(0, 100)..Coordinate(0,150)).zip(Coordinate(50,50)..Coordinate(50,0)) // REVERSE
            val l = (Coordinate(0,100)..Coordinate(50,100)).zip(Coordinate(50,50)..Coordinate(50,100)) // RIGHT, LEFT
        }

        @Test
        internal fun `part 2`() {
            val board = Board(input, true)

            val (position, facing) = board.followInstructions()
            assertThat(password(position, facing)).isEqualTo(5031)
        }
    }

    private val input by lazy { readFile("/input-day22.txt")}

}
