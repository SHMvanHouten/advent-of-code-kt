package com.github.shmvanhouten.adventofcode2023.day16

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day16Test {

    val example = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()

    @Nested
    inner class Part1 {

        @Test
        internal fun `example 1`() {
            val energized = energizeFromTopLeftGoingRight(example)
            println(energized.map { it.toChar() })
            expect {
                that(energized.map { it.toChar() }.toString())
                    .isEqualTo("""
                        >|<<<\....
                        |v-.\^....
                        .v...|->>>
                        .v...v^.|.
                        .v...v^...
                        .v...v^..\
                        .v../2\\..
                        <->-/vv|..
                        .|<<<2-|.\
                        .v//.|.v..
                    """.trimIndent())

                that(energized.countEnergizedTiles())
                    .isEqualTo(46)
            }
        }

        @Test
        internal fun `part 1`() {
            expectThat(
                energizeFromTopLeftGoingRight(input).countEnergizedTiles()
            ).isEqualTo(7623)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `example 2`() {
            expectThat(findMostEnergized(example))
                .isEqualTo(51)
        }

        @Test
        internal fun `part 2`() {
            expectThat(findMostEnergized(input))
                .isEqualTo(8244)
        }
    }

    private val input by lazy { readFile("/input-day16.txt")}

}
