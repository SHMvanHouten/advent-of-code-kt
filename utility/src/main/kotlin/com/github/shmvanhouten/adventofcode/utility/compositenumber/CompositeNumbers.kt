package com.github.shmvanhouten.adventofcode.utility.compositenumber

import java.math.BigInteger

fun primeFactors(number: Long): List<Long> {
    val factors = mutableListOf<Long>()
    var remainder = number

    var divisor = 2L
    while (remainder > 1) {
        while (remainder % divisor == 0L) {
            factors.add(divisor)
            remainder /= divisor
        }
        divisor++
    }
    return factors
}

fun greatestCommonDivisor(numbers: List<Long>): Long {
    return numbers
        .map { BigInteger.valueOf(it) }
        .greatestCommonDivisor()
        .toLong()
}

fun List<BigInteger>.greatestCommonDivisor(): BigInteger {
    return this.reduce(BigInteger::gcd)
}

fun leastCommonMultiple(numbers: List<Long>): Long {
    return numbers.map { BigInteger.valueOf(it) }
        .leastCommonMultiple().toLong()
}

fun List<BigInteger>.leastCommonMultiple(): BigInteger {
    return this.reduce(::leastCommonMultiple)
}

fun leastCommonMultiple(one: BigInteger, other: BigInteger): BigInteger {
    return one.times(other).divide(one.gcd(other))
}
