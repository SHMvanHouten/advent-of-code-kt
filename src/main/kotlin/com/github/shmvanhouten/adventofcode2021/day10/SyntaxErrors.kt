package com.github.shmvanhouten.adventofcode2021.day10

import com.github.shmvanhouten.adventofcode2021.average.median

private const val openingCharacters = "([{<"
private const val closingCharacters = ")]}>"
private val scoresPart1 = mapOf(
    ')' to 3L,
    ']' to 57L,
    '}' to 1197L,
    '>' to 25137L
)

private val scoresPart2 = mapOf(
    ')' to 1L,
    ']' to 2L,
    '}' to 3L,
    '>' to 4L,
)

fun scoreInputForCompleteness(input: List<String>): Long {
    return listIncompleteLines(input)
        .map { completeLine(it) }
        .map { score(it) }
        .median()
}

fun score(completingBrackets: String): Long {
    return completingBrackets.fold(0L) { score, c ->
        score * 5 + scoresPart2[c]!!
    }
}

fun listIncompleteLines(lines: List<String>): List<String> {
    return lines.filter { firstIllegalCharacter(it) == null }
}

fun completeLine(line: String): String {
    var closeNext = ""
    for (c in line) {
        when {
            c in openingCharacters ->
                closeNext += c

            closeNext.isEmpty() -> {
                // do nothing
            }

            findMatchingOpeningCharacter(c) == closeNext.last() ->
                closeNext = closeNext.substring(0, closeNext.lastIndex)
        }

    }
    return closeNext.reversed()
        .map { findMatchingClosingCharacter(it) }.joinToString("")
}

fun scoreLinesForSyntaxErrors(lines: List<String>): Long {
    val mapNotNull = lines.mapNotNull { firstIllegalCharacter(it) }
    return mapNotNull
        .sumOf { scoresPart1[it]!! }
}

fun firstIllegalCharacter(input: String): Char? {
    var closeNext = ""
    for (c in input) {
        when {
            c in openingCharacters ->
                closeNext += c

            findMatchingOpeningCharacter(c) == closeNext.last() ->
                closeNext = closeNext.substring(0, closeNext.lastIndex)

            else ->
                return c
        }

    }
    return null
}

private fun findMatchingOpeningCharacter(c: Char) = openingCharacters[closingCharacters.indexOf(c)]
private fun findMatchingClosingCharacter(c: Char) = closingCharacters[openingCharacters.indexOf(c)]
