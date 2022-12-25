package com.github.shmvanhouten.adventofcode2022.day25

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day25Test {

    @Nested
    inner class Part1 {

        @ParameterizedTest(name = "SNAFU nr {0} is {1}")
        @CsvSource(
            delimiter = ',', value = [
                "0,0",
                "1,1",
                "2,2",
                "1=,3",
                "1-,4",
                "10,5",
                "11,6",
                "12,7",
                "2=,8",
                "2-,9",
                "20,10",
                "1=0,15",
                "1-0,20",
                "1=11-2,2022",
                "1-0---0,12345",
                "1121-1110-1=0,314159265",
            ]
        )
        internal fun `SNAFU number is in decimal`(snafu: String, decimal: Long) {
            expectThat(toDecimal(snafu)).isEqualTo(decimal)
        }

        @ParameterizedTest
        @CsvSource(
            delimiter = ',', value = [
                "1,1",
                "2,2",
                "3,1=",
                "4,1-",
                "5,10",
                "6,11",
                "7,12",
                "8,2=",
                "9,2-",
                "10,20",
                "15,1=0",
                "20,1-0",
                "2022,1=11-2",
                "12345,1-0---0",
                "314159265,1121-1110-1=0",
            ]
        )
        fun `decimal number is in SNAFU`(decimal: Long, snafu: String) {
            expectThat(toSnafu(decimal)).isEqualTo(snafu)
        }

        @Test
        internal fun example() {
            val input = """
                1=-0-2
                12111
                2=0=
                21
                2=01
                111
                20012
                112
                1=-1=
                1-12
                12
                1=
                122
            """.trimIndent()

            val result = input.lines().map { toDecimal(it) }.sum()
            expectThat(result)
                .isEqualTo(4890)
            expectThat(toSnafu(result))
                .isEqualTo("2=-1=0")
        }

        @Test
        internal fun `part 1`() {
            val result = input.lines().map { toDecimal(it) }.sum()
            expectThat(result)
                .isEqualTo(30332970236150)
            expectThat(toSnafu(result))
                .isEqualTo("2=0--0---11--01=-100")
        }
    }

    private val input by lazy { readFile("/input-day25.txt") }

}
