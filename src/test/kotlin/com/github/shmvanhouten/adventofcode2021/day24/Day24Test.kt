package com.github.shmvanhouten.adventofcode2021.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day24Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            printFirstPossibilities()

            val result = (9..13).reversed().fold(0L) { target, i ->
                listNecessaryDigitsToAttainTarget(target, i).first()
            }
            println(result)
//            assertThat(countDownUntilValid(), equalTo("1") )
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `fixme`() {
            assertThat(1, equalTo(1) )
        }

        @Test
        internal fun `part 2`() {
            assertThat(1, equalTo(1) )
        }
    }

    private val input by lazy { readFile("/input-day24.txt")}

}
