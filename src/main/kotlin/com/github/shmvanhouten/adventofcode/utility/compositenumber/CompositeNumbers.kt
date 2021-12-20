package com.github.shmvanhouten.adventofcode.utility.compositenumber

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

    var lists = primeFactorsPerNumber.map { it.toMutableList() }
    val factors = mutableListOf<Long>()
    while (lists.isNotEmpty()) {
        val factor = lists.map { it.first() }.minOrNull()!!
        factors += factor
        lists.filter { it.contains(factor) }.forEach { it -= factor }
        lists = lists.filter { it.isNotEmpty() }
    }
    return  factors
}