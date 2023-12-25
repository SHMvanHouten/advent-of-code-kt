package com.github.shmvanhouten.adventofcode2023.day24

import org.junit.jupiter.api.Test

class RockThrowingTest {

    private val answer = """
        24, 13, 10 @ -3, 1, 2""".trimIndent()

    private val example = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Test
    fun `if we treat our rock as standing still, we can`() {
        findRockThatHitsEveryStone(example)
    }

    @Test
    fun `given these inputs, we should get to 10`() {
        val input = """
            30, -4
            22, -4
            34, -6
            28, -3
            15, -5
        """.trimIndent()
    }
}