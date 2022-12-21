package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day21Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun example() {
            assertThat(shout(example)).isEqualTo(152)
        }

        @Test
        internal fun `part 1`() {
            assertThat(shout(input)).isEqualTo(31017034894002)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `another test`() {
            val test =  "(((4+(3*(humn-3)))/4)*6)=96"
//            val test =  "((4+(3*(23-3)))/4)=114"
            assertThat(humanValue(test)).isEqualTo(23)
        }

        @Test
        internal fun `example 2`() {
            val example = """
                root: juli + josi
                juli: amee + alex
                amee: buki * abby
                buki: 5
                abby: 4
                alex: 4
                josi: benj / mark
                benj: 360
                mark: emly - humn
                emly: 34
                humn: 0
            """.trimIndent()
            // 24=(360/(34-humn))
            assertThat(humanMonkeyValue(example)).isEqualTo(19)
        }

        @Test
        internal fun `part 2 print`() {
            println(printMonkeys(input))
        }

        @Test
        internal fun example() {
            assertThat(humanMonkeyValue(example)).isEqualTo(301)
        }

        @Test
        internal fun part1() {
            assertThat(humanMonkeyValue(input)).isEqualTo(3555057453229)
        }
    }

    private val input by lazy { readFile("/input-day21.txt")}
    private val example = """
        root: pppw + sjmn
        dbpl: 5
        cczh: sllz + lgvd
        zczc: 2
        ptdq: humn - dvpt
        dvpt: 3
        lfqf: 4
        humn: 5
        ljgn: 2
        sjmn: drzm * dbpl
        sllz: 4
        pppw: cczh / lfqf
        lgvd: ljgn * ptdq
        drzm: hmdt - zczc
        hmdt: 32
    """.trimIndent()

}
