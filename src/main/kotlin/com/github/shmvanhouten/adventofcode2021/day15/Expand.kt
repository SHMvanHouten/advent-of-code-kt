package com.github.shmvanhouten.adventofcode2021.day15

fun expand(input: String): String {
    val expandedLines = input.lines().map { expandLine(it) }
    return expandDownwards(expandedLines)
}

fun expandDownwards(expandedLines: List<List<Int>>): String {
    return 0.until(4).runningFold(expandedLines) { lines, _ ->
        lines.map { it.map { risk -> transform(risk) } }
    }.flatMap { transformedMap -> transformedMap.map { it.joinToString("") } }
        .joinToString("\n")
}

fun expandLine(line: String): List<Int> {
    return (0.until(5))
        .flatMap { line.map { it.digitToInt() }.transform(it) }
}

private fun List<Int>.transform(amount: Int): List<Int> {
    return this.map { transform(it, amount) }
}

private fun transform(risk: Int, amount: Int = 1) = ((risk - 1 + amount) % 9) + 1
