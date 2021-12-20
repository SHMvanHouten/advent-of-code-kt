package com.github.shmvanhouten.adventofcode.utility.compositenumber

import com.github.shmvanhouten.adventofcode.utility.collectors.toMap
import java.math.BigInteger
import kotlin.math.max

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
    if (remainder > 1) {
        factors.add(remainder)
    }
    return factors
}

fun leastCommonMultiple(ns: List<Long>): Long {
    return removeDuplicates(ns.map { primeFactors(it) })
        .reduce { acc: Long, l: Long -> acc * l }
}

fun removeDuplicates(primeFactorsPerNumber: List<List<Long>>): List<Long> {
    return primeFactorsPerNumber
        .map { primes -> primes.groupBy { it }.entries }
        .flatten()
        .map { (key, value) -> key to value.size }
        .toMap { one, other -> max(one, other) }
        .map { (n, factor) -> BigInteger.valueOf(n).pow(factor) }
        .map { it.toLong() }
}