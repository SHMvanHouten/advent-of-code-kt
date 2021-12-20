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
        fun leastCommonMultiple(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(4L, 6L, 8L), 2*2*2*3L),
                Arguments.of(listOf(18L, 44L, 28L), 2772L),
                Arguments.of(listOf(140, 72), 2520),
                Arguments.of(listOf(288, 420), 10080),
                Arguments.of(listOf(140, 15), 420),
            )

        @Suppress("unused")
        @JvmStatic
        fun greatestCommonDivisor(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(72, 48, 54), 6),
                Arguments.of(listOf(288, 420), 12),
                Arguments.of(listOf(140, 72), 4),
                Arguments.of(listOf(2*3*5L, 2*5*7L, 5*7*11L), 5),
                Arguments.of(listOf(2*7*11*101, 2*37*101), 2 * 101),
                Arguments.of(listOf(2*7*11*101, 2*37*101, 7*11*37*101), 101),
                Arguments.of(listOf(2*7*11*101*87, 2*37*101*103, 7*11*37*101*723), 101),
                Arguments.of(listOf(2*7*11*101*87, 2*37*101*103*87, 7*11*101*723*87), 8787),
            )
    }

    @ParameterizedTest(name = "greatest common divisor between {0} is {1}")
    @MethodSource("greatestCommonDivisor")
    internal fun `greatest common divisor`(
        numbers: List<Long>,
        expected: Long
    ) {
        assertThat(greatestCommonDivisor(numbers), equalTo(expected))
    }

    @ParameterizedTest(name = "least common multiple between {0} is {1}")
    @MethodSource("leastCommonMultiple")
    internal fun `least common multiple`(
        numbers: List<Long>,
        expected: Long
    ) {
        assertThat(leastCommonMultiple(numbers), equalTo(expected))
    }

}