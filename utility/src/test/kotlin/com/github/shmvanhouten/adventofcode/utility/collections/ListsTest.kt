package com.github.shmvanhouten.adventofcode.utility.collections

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ListsTest {

    @Test
    fun `combines all elements in a list to pairs`() {
        expectThat(listOf(1, 2, 3, 4).combineAll())
            .isEqualTo(listOf(1 to 2, 1 to 3, 1 to 4, 2 to 3, 2 to 4, 3 to 4))
    }

    @Test
    fun `combines all elements and uses the transform on the pairs`() {
        expectThat(listOf(1, 2, 3, 4).combineAllWith(Int::minus))
            .isEqualTo(listOf(-1, -2, -3, -1, -2, -1))
    }
}