package com.github.shmvanhouten.adventofcode2024.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

fun main() {
    val input = readFile("/input-day03.txt")

    val pt1 = calculateAllProducts(input)

    val pt2 = calculateProductsInDoRanges(input)
    println(pt2)
}

fun calculateProductsInDoRanges(
    input: String
): Long {
    val productsToResults = findProducts(input)
    val doRanges = findDoRanges(input)

    return productsToResults.filter { (value, _) ->
        doRanges.any { input.indexOf(value) in it }
    }
        .map { it.second }
        .sum()
}

fun calculateAllProducts(input: String): Long {
    val productsToResults = findProducts(input)
    return productsToResults.map { it.second }
        .sum()
}

private fun findDoRanges(input: String): List<IntRange> {
    val dos = input.indicesOfAll("do()")
    val donts = input.indicesOfAll("don't()")

    val doRanges = findDoRanges(dos, donts, input.length)
    return doRanges
}

fun findDoRanges(dos: List<Int>, donts: List<Int>, length: Int): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var lastCanDoIndex: Int? = 0
    (dos.map { it to true } + donts.map { it to false })
        .sortedBy { it.first }
        .forEach { (index, canDo) ->
            if(lastCanDoIndex != null && !canDo) {
                ranges += (lastCanDoIndex!!..index)
                lastCanDoIndex = null

            } else if(lastCanDoIndex == null && canDo) {
                lastCanDoIndex = index
            }
        }

    if(lastCanDoIndex != null) {
        ranges += lastCanDoIndex!!..length
    }
    return ranges.toList()
}

private fun String.indicesOfAll(search: String): List<Int> {
    var nextIndex = indexOf(search)
    val results = mutableListOf<Int>()
    while (nextIndex != -1) {
        results += nextIndex
        nextIndex = indexOf(search, nextIndex + 1)
    }
    return results.toList()
}

private fun findProducts(input: String): Sequence<Pair<String, Long>> {
    val regex = Regex("""mul\((\d)*,(\d)*\)""")
    val sums = regex.findAll(input)
    return sums.map { it.value }
        .map { it to it.calculateProduct() }
}

fun String.calculateProduct(): Long {
    return substringBetween("mul(", ")").split(',').map { it.toLong() }.product()
}

