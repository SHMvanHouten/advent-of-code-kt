package com.github.shmvanhouten.adventofcode2021.day03

fun findGammaAndEpsilonRate(lines: List<String>): Pair<Int, Int> {
    val gamma = findGammaRate(lines)
    return gamma to getMaxBinary(lines).xor(gamma)
}

private fun findGammaRate(lines: List<String>): Int {
    return 0.until(lines[0].length)
        .map { i -> lines.count { it[i] == '1' } }
        .map { it > lines.size / 2 }
        .map { boolToBit(it) }
        .joinToString("")
        .toInt(2)
}

private fun boolToBit(bool: Boolean) = if (bool) '1' else '0'

private fun getMaxBinary(lines: List<String>) =
    lines[0].map { '1' }.joinToString("").toInt(2)

fun findOxygenGeneratorAndCO2Scrubber(lines: List<String>): Pair<Int, Int> {
    return findOxygenGeneratorRate(lines) to findC02ScrubberRate(lines)
}

private fun findOxygenGeneratorRate(lines: List<String>): Int {
    return filterBinaryStringForCriteria(lines) { bit, i, remainingLines ->
        bit == findMostCommonBitAtIndex(remainingLines, i)
    }
}

private fun findC02ScrubberRate(lines: List<String>): Int {
    return filterBinaryStringForCriteria(lines) { bit, i, remainingLines ->
        bit == findLeastCommonBitAtIndex(remainingLines, i)
    }
}

private fun filterBinaryStringForCriteria(
    lines: List<String>,
    bitMatchesCriteria: (bit: Char, index: Int, lines: List<String>) -> Boolean
): Int {
    var remainingLines = lines
    0.until(lines[0].length).forEach { i ->
        remainingLines = remainingLines.filter { bitMatchesCriteria(it[i], i, remainingLines) }
        if (remainingLines.size == 1) {
            return remainingLines.first().toInt(2)
        }
    }
    error("Something went wrong, no line won: remaining lines: $remainingLines")
}

private fun findMostCommonBitAtIndex(lines: List<String>, index: Int): Char {
    val (ones, zeroes) = countOnesAndZeroes(lines, index)

    return if (ones >= zeroes) '1'
    else '0'
}

private fun findLeastCommonBitAtIndex(lines: List<String>, index: Int): Char {
    val (ones, zeroes) = countOnesAndZeroes(lines, index)

    return if (ones < zeroes) '1'
    else '0'
}

private fun countOnesAndZeroes(
    lines: List<String>,
    index: Int
): Pair<Int, Int> {
    return countOnesAndZeroes(lines.map { it[index] })
}

private fun countOnesAndZeroes(bits: List<Char>) =
    bits.count { it == '1' } to
            bits.count { it == '0' }
