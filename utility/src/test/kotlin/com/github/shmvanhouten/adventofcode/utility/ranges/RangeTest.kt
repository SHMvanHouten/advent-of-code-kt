package com.github.shmvanhouten.adventofcode.utility.ranges

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class RangeTest {

    @Test
    internal fun `1 to 3 does not contain 4 to 5`() {
        assertThat(
            (1..3).contains(4..5),
            equalTo(false)
        )
    }

    @Test
    internal fun `4 to 5 does not contain 1 to 3`() {
        assertThat(
            (4..5).contains(1..3),
            equalTo(false)
        )
    }

    @Test
    internal fun `1 to 5 contains 2 to 3`() {
        assertThat(
            (1..5).contains(2..3),
            equalTo(true)
        )
    }

    @Test
    internal fun `1 to 5 does not contain 2 to 6`() {
        assertThat(
            (1..5).contains(2..6),
            equalTo(false)
        )
    }
}