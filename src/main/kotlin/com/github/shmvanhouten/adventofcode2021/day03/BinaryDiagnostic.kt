package com.github.shmvanhouten.adventofcode2021.day03

fun findGammaAndEpsilonRate(lines: List<String>): Pair<Int, Int> {
    val gamma = findGammaRate(lines)
    return gamma to getMaxBinary(lines).xor(gamma)
}

private fun getMaxBinary(lines: List<String>) =
    lines[0].map { '1' }.joinToString("").toInt(2)

fun findGammaRate(lines: List<String>): Int {
    return 0.until(lines[0].length)
        .map { i -> lines.count { it[i] == '1' } }
        .map { it > lines.size/2 }
        .map { if(it) '1' else '0' }
        .joinToString("")
        .toInt(2)
}

fun findEpsilonRate(lines: List<String>): Int {
    return -1
}
