package com.github.shmvanhouten.adventofcode2021.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
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
                """.trimMargin()
            val (enhancementString, image) = parse(example)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString)
            assertThat(draw(enhancedImage, '#'), equalTo(expected))
            assertThat(enhanceImage(image, enhancementString, 2).first.size, equalTo(35))
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
                """.trimIndent()
            val (enhancementString, image) = parse(example)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString, true)
            assertThat(draw(enhancedImage, '#'), equalTo(expected))
        }

        @Test
        internal fun `part 1`() {
            val (enhancementString, image) = parse(input)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString)
            assertThat(surroundingPixelsAreLit, equalTo(true))
            val (doubleEnhanced, surroundingPixelsAreLitAgain) = enhanceImage(enhancedImage, enhancementString, surroundingPixelsAreLit)
            assertThat(surroundingPixelsAreLitAgain, equalTo(false))
            assertThat(doubleEnhanced.size, equalTo(1))
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val (enhancementString, image) = parse(example)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString, 50)
            assertThat(enhancedImage.size, equalTo(3351))
        }

        @Test
        internal fun `part 2`() {
            val (enhancementString, image) = parse(input)
            val (enhancedImage, surroundingPixelsAreLit) = enhanceImage(image, enhancementString, 50)
            assertThat(enhancedImage.size, equalTo(3351))
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
