package com.github.shmvanhouten.adventofcode2021.day08

fun countUniquedigits(input: Digit): Int {
    val signals = input.lines().map { toSignal(it) }
    return signals.map { it.output }
        .sumOf { output -> countUniqueDigits(output) }
}

private fun countUniqueDigits(output: List<Digit>) =
    output.count { signal -> isADigitWithUniqueSegmentCount(signal) }

private fun isADigitWithUniqueSegmentCount(digit: Digit): Boolean {
    val length = digit.length
    return length == 2 || length == 3 || length == 4 || length == 7
}
