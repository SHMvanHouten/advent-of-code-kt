package com.github.shmvanhouten.adventofcode.utility.compositenumber

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class PrimeFactorTest {

    @Test
    internal fun `the prime factors of 1 is an empty list`() {
        assertThat(primeFactors(1), equalTo(emptyList()))
    }

    @Test
    internal fun `the prime factor of 2 is 2`() {
        assertThat(primeFactors(2), equalTo(listOf(2L)))
    }

    @Test
    internal fun `the pri factor of 3 is 3`() {
        assertThat(primeFactors(3), equalTo(listOf(3L)))
    }

    @Test
    internal fun `the prime factor of 4 is 2, 2`() {
        assertThat(primeFactors(4), equalTo(listOf(2L, 2L)))
    }

    @Test
    internal fun `more prime factors`() {
        assertThat(primeFactors(5), equalTo(listOf(5L)))
        assertThat(primeFactors(6), equalTo(listOf(2L, 3L)))
        assertThat(primeFactors(7), equalTo(listOf(7L)))
        assertThat(primeFactors(8), equalTo(listOf(2L, 2L, 2L)))
    }

    @Test
    internal fun `prime factors over 9`() {
        assertThat(primeFactors(9), equalTo(listOf(3L, 3L)))
        assertThat(primeFactors(10), equalTo(listOf(2L, 5L)))
    }

    @Test
    internal fun `big test`() {
        assertThat(primeFactors(2 * 2 * 3 * 4 * 11 * 13 * 983), equalTo(listOf(2L, 2L, 2L, 2L, 3L, 11L, 13L, 983L)))
    }


    companion object {
        @Suppress("unused")
        @JvmStatic
        fun commonMultiples(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(4L, 6L, 8L), 2*2*2*3L),
                Arguments.of(listOf(18L, 44L, 28L), 2772L),
                Arguments.of(listOf(140, 72), 2520),
                Arguments.of(listOf(288, 420), 10080)
            )
    }

    @ParameterizedTest
    @MethodSource("commonMultiples")
    internal fun `least common multiple of 3, 4 and 6 is 12`(
        numbers: List<Long>,
        expected: Long
    ) {
        assertThat(leastCommonMultiple(numbers), equalTo(expected))
    }
}