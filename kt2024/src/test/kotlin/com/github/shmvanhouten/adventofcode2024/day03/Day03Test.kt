package com.github.shmvanhouten.adventofcode2024.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day03Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            expectThat(calculateAllProducts(input)).isEqualTo(168539636L)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            expectThat(calculateProductsInDoRanges(input)).isEqualTo(97529391L)
        }
    }

    private val input by lazy { readFile("/input-day03.txt")}

}
