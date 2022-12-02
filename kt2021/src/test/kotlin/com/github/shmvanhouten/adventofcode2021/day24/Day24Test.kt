package com.github.shmvanhouten.adventofcode2021.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class Day24Test {

    @Nested
    inner class TestAluRunner {
        @Test
        internal fun `14992994195999 is a valid model number`() {
            val number = "14992994195999"
            val alu = AluRunnner(input)
            val result: Long = alu.check(number)
            assertThat(result, equalTo(14)) //???
            assertThat(findOutComeOfNumber(number), equalTo(14))
        }


    }

    @Test
    internal fun `how does 1404 equate to 36646`() {
        // to get 1409 with w = 2 (and r = -10) how do we get 1404?
        assertThat(handleDigitTypeB(2, -10, 3, 1404L), equalTo(1409))
        assertThat(handleDigitTypeB(2, -10, 3, 36646), equalTo(1409))

        // if "doComparison" equates to 1 (so z % 26 + r != w
        // z + w + s
        // any value for z where (z % 26) - 10 != w
        // AND z + w + s == 1409


        println(bReverseFunction(2, -10, 3, 1409))
        println(bReverseFunction(3, -10, 3, 1409))


        doReverseFunctionBAtIndex(10, 1404L)
            .forEach { println(it) }

//            println(leastCommonMultiple(listOf(1404L, 36646)))
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "94992994195998",
            "21191861151161"
        ]
    )
    internal fun `testing outcomes`(nr: String) {
        assertThat(findOutComeOfNumber(nr), equalTo(0))
    }

    @Test
    internal fun `part 1 and 2`() {
//            printFirstPossibilities()

        val listValidNumbers = listValidNumbers()
        listValidNumbers.forEach {
            assertThat(findOutComeOfNumber(it), equalTo(0))
        }
        assertThat(listValidNumbers.maxOrNull(), equalTo("94992994195998"))
        assertThat(listValidNumbers.minOrNull(), equalTo("21191861151161"))
//            assertThat(countDownUntilValid(), equalTo("1") )
        // 14992994195999 too low
    }

    private val input by lazy { readFile("/input-day24.txt") }

}
