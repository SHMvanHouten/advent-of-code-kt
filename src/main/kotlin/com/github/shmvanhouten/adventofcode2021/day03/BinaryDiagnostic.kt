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
    return filterBinaryStringForCriteria(lines) { bit, bits ->
        bit == findMostCommonBitAtIndex(bits)
    }
}

private fun findC02ScrubberRate(lines: List<String>): Int {
    return filterBinaryStringForCriteria(lines) { bit, bits ->
        bit == findLeastCommonBitAtIndex(bits)
    }
}

private fun filterBinaryStringForCriteria(
    lines: List<String>,
    bitMatchesCriteria: (bit: Char, bits: List<Char>) -> Boolean
): Int {
    return 0.until(lines[0].length)
        .runningFold(lines) { remainingLines, i ->
            remainingLines.filter { bitMatchesCriteria(it[i], listBitsAtIndex(remainingLines, i)) }
        }.asSequence()
        .dropWhile { it.size > 1 }.first()
        .first().toInt(2)
}

private fun listBitsAtIndex(
    remainingLines: List<String>,
    i: Int
) = remainingLines.map { it[i] }

private fun findMostCommonBitAtIndex(bits: List<Char>): Char {
    val (ones, zeroes) = countOnesAndZeroes(bits)

    return boolToBit(ones >= zeroes)
}

private fun findLeastCommonBitAtIndex(bits: List<Char>): Char {
    val (ones, zeroes) = countOnesAndZeroes(bits)

    return boolToBit(ones < zeroes)
}

private fun countOnesAndZeroes(bits: List<Char>) =
    bits.count { it == '1' } to
            bits.count { it == '0' }
