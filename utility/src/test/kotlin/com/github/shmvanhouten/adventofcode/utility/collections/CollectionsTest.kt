package com.github.shmvanhouten.adventofcode.utility.collections

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CollectionsTest {
    @Nested
    inner class CountDuplicates {
        @Test
        fun `counts the duplicates in a list`() {
            val list = listOf(1, 1, 5, 2, 5, 9, 1, 8, 2)
            expectThat(list.countDuplicates()).isEqualTo(3)
        }

        @Test
        fun `counts the triplicates or more if indicated`() {
            val list = listOf(1, 1, 5, 2, 5, 9, 1, 8, 2)
            expectThat(list.countDuplicates(minNrOfDuplications = 3)).isEqualTo(1)
        }
    }
}