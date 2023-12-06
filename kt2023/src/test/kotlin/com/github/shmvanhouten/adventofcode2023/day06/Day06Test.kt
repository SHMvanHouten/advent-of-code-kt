package com.github.shmvanhouten.adventofcode2023.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.productOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day06Test {

    private val example = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()
    @Nested
    inner class Part1 {

        @Test
        internal fun `fixme`() {
            val highscores = toRaceHighscores(example)
            expect {
                that(highscores.first().waysToBeat())
                    .isEqualTo(4)
                that(highscores[1].waysToBeat())
                    .isEqualTo(8)
                that(highscores[2].waysToBeat())
                    .isEqualTo(9)
                that(highscores.productOf { it.waysToBeat() })
                    .isEqualTo(288)
            }
        }

        @Test
        internal fun `part 1`() {
            val highscores = toRaceHighscores(input)
            expectThat(highscores.productOf { it.waysToBeat() })
                .isEqualTo(771628)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(Highscore(71530, 940200).waysToBeat())
                .isEqualTo(71503)

        }

        @Test
        internal fun `part 2`() {
            expectThat(Highscore(41777096,249136211271011).waysToBeat())
                .isEqualTo(27363861)
        }
    }

    private val input by lazy { readFile("/input-day06.txt")}

}
