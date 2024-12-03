package com.github.shmvanhouten.adventofcode2024.day03

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

fun main() {
    val regex = Regex("""mul\((\d)*,(\d)*\)""")
    val input = readFile("/input-day03.txt")
    val find = regex.findAll(input)
    val results = find.map {
        it.value
    }
    println(results.count())
    results.map { it.parse() }.sum().also { println(it) }

}

fun String.parse(): Long {
    return substringBetween("mul(", ")").split(',').map { it.toLong() }.product()
}

