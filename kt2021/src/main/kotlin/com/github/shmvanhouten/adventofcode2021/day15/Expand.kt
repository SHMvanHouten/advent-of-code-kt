package com.github.shmvanhouten.adventofcode2021.day15

fun expand(input: String): String {
    return input.lines()
        .map { it.map { it.digitToInt() } }
        .map { expandRows(it) }
        .let { expandColumns(it) }
        .map { line -> line.joinToString("") }
        .joinToString("\n")
}

fun expandColumns(expandedLines: List<List<Int>>): List<List<Int>> {
    return 0.until(4).runningFold(expandedLines) { lines, _ ->
        lines.map { it.map { risk -> transform(risk) } }
    }.flatMap { transformedMap -> transformedMap.map { it } }
}

fun expandRows(line: List<Int>): List<Int> {
    return (0.until(5))
        .flatMap { line.transform(it) }
}

private fun List<Int>.transform(amount: Int): List<Int> {
    return this.map { transform(it, amount) }
}

private fun transform(risk: Int, amount: Int = 1) = ((risk - 1 + amount) % 9) + 1
