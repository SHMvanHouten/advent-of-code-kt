package com.github.shmvanhouten.adventofcode2024.day19

import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun countPossibleDesigns(input: String): List<String> {
    val (availablePatterns, wantedLines) = parse(input)
    return wantedLines.filter { countWaysToCreate(it, availablePatterns) > 0 }
}

fun countWaysToCreateAllDesigns(input: String): Long {
    val (availablePatterns, wantedLines) = parse(input)

    return wantedLines.sumOf { countWaysToCreate(it, availablePatterns) }
}

fun countWaysToCreate(wanted: String, availableTowels: List<String>): Long {
    val availablePatternsSoFar = mutableMapOf("" to 1L)
    var found = 0L
    while (availablePatternsSoFar.isNotEmpty()) {
        val (stringSoFar, waysToCreate) = availablePatternsSoFar.removeFirst()
        val remaining = wanted.substringAfter(stringSoFar)
        if(remaining.isEmpty()) found += waysToCreate
        availableTowels
            .filter(remaining::startsWith)
            .map { stringSoFar + it }
            .forEach {
                val waysToCreateAlready = availablePatternsSoFar.getOrDefault(it, 0L)
                availablePatternsSoFar[it] = (waysToCreateAlready + waysToCreate)
            }
    }
    return found
}

fun MutableMap<String, Long>.removeFirst(): Map.Entry<String, Long> {
    val entry = entries.first()
    remove(entry.key)
    return entry
}

fun parse(input: String): Pair<List<String>, List<String>> {
    val (patterns, wanted) = input.blocks()
    return patterns.split(", ").sortedBy { it.length } to wanted.lines()
}