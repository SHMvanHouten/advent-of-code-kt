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
        internal fun `fixme`() {
            assertThat(1).isEqualTo(1)
        }

        @Test
        internal fun `part 2`() {
            assertThat(1).isEqualTo(1)
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
