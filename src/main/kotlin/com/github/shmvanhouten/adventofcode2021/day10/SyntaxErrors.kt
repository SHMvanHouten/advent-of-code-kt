package com.github.shmvanhouten.adventofcode2021.day10

private const val openingCharacters = "([{<"
private const val closingCharacters = ")]}>"
private val scores = mapOf(
    ')' to 3L,
    ']' to 57L,
    '}' to 1197L,
    '>' to 25137L
)

fun scoreLinesForSyntaxErrors(lines: List<String>): Long {
    val mapNotNull = lines.mapNotNull { firstIllegalCharacter(it) }
    return mapNotNull
        .sumOf { scores[it]!! }
}

fun firstIllegalCharacter(input: String): Char? {
    var closeNext = ""
    for (c in input) {
        if (c in openingCharacters) {
            closeNext += c

        } else if (findMatchingClosingCharacter(c) == closeNext.last()) {
            closeNext = closeNext.substring(0, closeNext.lastIndex)

        } else {
            return c
        }

    }
    return null
}

private fun findMatchingClosingCharacter(c: Char) = openingCharacters[closingCharacters.indexOf(c)]

private fun Char.occursLessThanOtherChars(charCounts: Map<Char, Int>): Boolean {
    val thisCharCount = charCounts[this] ?: 0
    val otherCharCounts = charCounts.minus(this).values
    return otherCharCounts.any { it > thisCharCount }
}
