package com.github.shmvanhouten.adventofcode2024.day19

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun main() {
    readFile("/input-day19.txt")
        .lines()
        .onEach(::println)
}

fun countPossibleDesigns(input: String): List<String> {
    val (availablePatterns, wantedLines) = parse(input)
    return wantedLines.filter { isPossible(it, availablePatterns) }
}

fun countWaysToCreateAllDesigns(input: String): Long {
    val (availablePatterns, wantedLines) = parse(input)

    return wantedLines.sumOf { countWaysToCreate(it, availablePatterns) }
}

fun countWaysToCreate(wanted: String, availableTowels: List<String>): Long {
    val availablePatternsSoFar = mutableMapOf("" to 1L)
    var found = 0L
    while (availablePatternsSoFar.isNotEmpty()) {
        val (stringSoFar, waysToCreate) = availablePatternsSoFar.entries.first()
        availablePatternsSoFar.remove(stringSoFar)
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

fun isPossible(wanted: String, availableTowels: List<String>): Boolean {
    val availablePatternsSoFar = mutableSetOf(0)
    while (availablePatternsSoFar.isNotEmpty()) {
        val soFar = availablePatternsSoFar.first()
        availablePatternsSoFar.remove(soFar)
        val remaining = wanted.substring(soFar)
        if(remaining.isEmpty()) return true
        availablePatternsSoFar.addAll(
            availableTowels
                .filter(remaining::startsWith)
                .map { soFar + it.length }
        )
    }
    return false
}

fun parse(input: String): Pair<List<String>, List<String>> {
    val (patterns, wanted) = input.blocks()
    return patterns.split(", ").sortedBy { it.length } to wanted.lines()
}