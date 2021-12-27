package com.github.shmvanhouten.adventofcode2021.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import com.github.shmvanhouten.adventofcode.utility.coordinate.toCoordinateMap
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day20Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            val expected = """
                | ## ## 
                |#  # # 
                |## #  #
                |####  #
                | #  ## 
                |  ##  #
                |   # # 
                """.trimMargin().toCoordinateMap('#')
            val (enhancementString, image) = parse(example)
            val (enhancedImage, _) = enhanceImage(image, enhancementString)
            assertThat(draw(enhancedImage, '#'), equalTo(draw(expected, '#')))
            assertThat(enhanceImage(image, enhancementString, 2).size, equalTo(35))
        }

        @Test
        internal fun `if the first char of the image enhancement string is a # then all surrounding pixels are #`() {
            val expected = """
                #########
                ## #    #
                ## ###  #
                ##  #  ##
                #  ##   #
                # #  ####
                #  #   ##
                ##  #####
                #########
                """.trimIndent().toCoordinateMap('#')
            val (enhancementString, image) = parse(example)
            val (enhancedImage, _) = enhanceImage(image.copy(surroundingPixelsAreLit = true), enhancementString)
            assertThat(draw(enhancedImage, '#'), equalTo(draw(expected, '#')))
        }

        @Test
        internal fun `part 1`() {
            val (enhancementString, image) = parse(input)
            val (doubleEnhanced, _) = enhanceImage(image, enhancementString, 2)
            assertThat(doubleEnhanced.size, equalTo(5461))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val (enhancementString, image) = parse(example)
            val (enhancedImage, _) = enhanceImage(image, enhancementString, 50)
            assertThat(enhancedImage.size, equalTo(3351))
        }

        @Test
        internal fun `part 2`() {
            val (enhancementString, image) = parse(input)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString, 50)
            assertThat(enhancedImage.size, equalTo(18226))
            assertThat(surroundingPixelsAreLit, equalTo(false))
        }
    }

    private val input by lazy { readFile("/input-day20.txt")}
    private val example = """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###"""
}
