package com.github.shmvanhouten.adventofcode2023.day01

fun sumCalibrationValues1(input: String) =
    input.lines()
        .map { line -> ("${line.first { it.isDigit() }}${line.last { it.isDigit() }}") }
        .sumOf { it.toInt() }

fun sumCalibrationValues2(input: String) = input.lines()
    .map { (firstNumber(it) + lastNumber(it)) }
    .sumOf { it.toInt() }

fun firstNumber(input: String, namedNumber: List<String> = nineWholeNumbers): String {
    val indexOfFirstDigit = input.indexOfFirst { it.isDigit() }
    val (indexOfFirstNr, firstNr) = input.findAnyOf(namedNumber)?: (Int.MAX_VALUE to "")

    return if (indexOfFirstDigit in 0..<indexOfFirstNr) {
        input[indexOfFirstDigit]
    } else {
        firstNr.stringToDigit(namedNumber)
    }.toString()
}

fun lastNumber(input: String): String =
    firstNumber(input.reversed(), nineWholeNumbersReversed)

private val nineWholeNumbers: List<String> = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private val nineWholeNumbersReversed = nineWholeNumbers.map { it.reversed() }
private fun String.stringToDigit(numbers: List<String>) = numbers.indexOf(this) + 1