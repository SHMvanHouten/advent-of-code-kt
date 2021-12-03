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

fun findOxygenGeneratorAndCO2Scrubber(lines: List<String>): Pair<Int, Int> {
    return findOxygenGeneratorRate(lines) to findC02ScrubberRate(lines)
}

fun findOxygenGeneratorRate(lines: List<String>): Int {
    var i = 0
    var remainingLines = lines
    while (remainingLines.size > 1) {
        val bit = findMostCommonBitAtIndex(remainingLines, i)
        remainingLines = remainingLines.filter { it[i] == bit }
        i++
    }
    return remainingLines[0].toInt(2)
}

fun findMostCommonBitAtIndex(lines: List<String>, index: Int) : Char {
    val ones = lines.count { it[index] == '1' }
    val zeroes = lines.count { it[index] == '0' }

    return if(ones >= zeroes) '1'
    else '0'
}

fun findC02ScrubberRate(lines: List<String>): Int {
    var i = 0
    var remainingLines = lines
    while (remainingLines.size > 1) {
        val bit = findLeastCommonBitAtIndex(remainingLines, i)
        remainingLines = remainingLines.filter { it[i] == bit }
        i++
    }
    return remainingLines[0].toInt(2)
}

fun findLeastCommonBitAtIndex(lines: List<String>, index: Int) : Char {
    val ones = lines.count { it[index] == '1' }
    val zeroes = lines.count { it[index] == '0' }

    return if(ones < zeroes) '1'
    else '0'
}
