package com.github.shmvanhouten.adventofcode.utility.ranges

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

class RangeMergeTest {

    @Test
    fun `two narrowly not overlapping ranges are not merged`() {
        expectThat(listOf(1L..2, 4L..5).merge())
            .hasSize(2)
            .and {
                first().isEqualTo(1L..2)
                get(1).isEqualTo(4L..5)
            }
    }

    @Test
    fun `two overlapping ranges are merged`() {
        expectThat(listOf(4L..10, 6L..11).merge())
            .hasSize(1)
            .and { first().isEqualTo(4L..11) }
    }

    @Test
    fun `two overlapping and one not result in the 2 merged but the other separate`() {
        expectThat(
            listOf(1..3L, 5.. 11L, 6..15L)
                .merge()
        )
            .hasSize(2)
            .and {
                any { isEqualTo(1..3L) }
                any { isEqualTo(5..15L) }
            }
    }

    @Test
    fun `two none overlapping but with one wedged in the middle result in one nice big range`() {
        expectThat(
            listOf(1..3L, 2..7L, 6..11L)
                .merge()
        )
            .hasSize(1)
            .and { first().isEqualTo(1..11L) }
    }

    @Test
    fun `two adjacent (but not overlapping) ranges still merge together`() {
        expectThat(
            listOf(1..3L, 4..5L).merge()
        )
            .hasSize(1)
            .and { first().isEqualTo(1..5L) }
    }

    @Test
    fun `a fully contained range is swallowed up`() {
        expectThat(
            listOf(5..6L, 4..9L).merge()
        )
            .hasSize(1)
            .and { first().isEqualTo(4L..9) }
    }
}