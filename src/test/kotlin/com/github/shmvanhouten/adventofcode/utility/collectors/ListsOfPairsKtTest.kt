package com.github.shmvanhouten.adventofcode.utility.collectors

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class ListsOfPairsKtTest {

    @Test
    internal fun `instead of dropping entries use the merge function to combine the values for the key`() {
        val pairs = listOf(
            "a" to 2,
            "b" to 3,
            "a" to 5,
            "c" to 2
        )
        val map = pairs.toMap(Int::plus)
        assertThat(map["a"], equalTo(5 + 2))
        assertThat(
            "normal map just drops the first key",
            pairs.toMap()["a"],
            equalTo(5)
        )
    }

    @Test
    internal fun `fracture splits the list of pairs into a pair of lists`() {
        val pairs = listOf(
            "a" to 2,
            "b" to 3,
            "a" to 5,
            "c" to 2
        )
        val (strings, ints) = pairs.fracture()
        assertThat(strings, equalTo(listOf("a", "b", "a", "c")))
        assertThat(ints, equalTo(listOf(2, 3, 5, 2)))
    }
}