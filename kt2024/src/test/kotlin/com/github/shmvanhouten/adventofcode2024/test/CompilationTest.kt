package com.github.shmvanhouten.adventofcode2024.test

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CompilationTest {
    @Test
    fun `it compiles`() {
        expectThat(1)
            .isEqualTo(1)
    }
}