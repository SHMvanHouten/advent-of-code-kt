package com.github.shmvanhouten.adventofcode.utility.compositenumber

import com.github.shmvanhouten.adventofcode.utility.collectors.toMap
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

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

fun greatestCommonDivisor(numbers: List<Long>): Long {
    return numbers
        .map { primeFactors(it) }
        .map { primes -> primes.groupBy { it }.entries }
        .map { primeGroupings -> primeGroupings.map { (key, value) -> key to value.size } }
        .joinShared()
        .map { (n, factor) -> BigInteger.valueOf(n).pow(factor) }
        .map { it.toLong() }
        .reduce(Long::times)
}

private fun List<List<Pair<Long, Int>>>.joinShared(): List<Pair<Long, Int>> {
    return this.reduce{ l1, l2 ->
        val other = l2.toMap()
        l1.filter { (nr, _) -> other.containsKey(nr) }
            .map { (nr, occurrence) -> nr to min(occurrence, other[nr]!!) }
    }
}

fun leastCommonMultiple(numbers: List<Long>): Long {
    return if(numbers.size == 2) numbers.reduce(Long::times).div(greatestCommonDivisor(numbers))
    else removeDuplicates(numbers.map { primeFactors(it) })
        .reduce (Long::times)
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