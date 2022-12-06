package com.github.shmvanhouten.adventofcode2022.day06

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day06Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `part 1`() {
            assertThat(whenIsStarterPacketProcessed(input, 4))
                .isEqualTo(1892)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `part 2`() {
            assertThat(whenIsStarterPacketProcessed(input, 14))
                .isEqualTo(2313)
        }
    }

    private val input by lazy { readFile("/input-day06.txt")}

}
