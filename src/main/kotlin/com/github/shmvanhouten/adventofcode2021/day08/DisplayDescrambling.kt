package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode2021.day08.Segment.*

fun decipherOutput(signal: Signal): Int {
    val mapping = findWireToSegmentMappings(signal.input)
    return signal.output.map { toDigit(it, mapping) }
        .joinToString("").toInt()
}

fun toDigit(hotWires: String, mapping: Map<Wire, Segment>): Int {
    return segmentsToDigitMapping[hotWires.map { mapping[it]!! }.toSet()]!!
}

fun findWireToSegmentMappings(input: List<String>): Map<Wire, Segment> {

    val one = input.single { it.length == 2 }
    val seven = input.single { it.length == 3 }
    val four = input.single { it.length == 4 }
    val eight = input.single { it.length == 7 }

    val zeroSixOrNine = input.filter { it.length == 6 }
    val nine = zeroSixOrNine.single { it.containsAllSegmentsOf(four) }
    val six = zeroSixOrNine.single { !it.containsAllSegmentsOf(one) }
    val zero = (zeroSixOrNine - nine - six).single()

    val twoThreeOfFive = input.filter { it.length == 5 }
    val three = twoThreeOfFive.single { it.containsAllSegmentsOf(one) }

    val bottomLeft  = theOneWireIn(eight).that(nine).doesNotHaveIs(BOTTOM_LEFT)
    val topRight    = theOneWireIn(eight).that(six).doesNotHaveIs(TOP_RIGHT)
    val middle      = theOneWireIn(eight).that(zero).doesNotHaveIs(MIDDLE)
    val top         = theOneWireIn(seven).that(one).doesNotHaveIs(TOP)
    val topLeft     = theOneWireIn(nine).that(three).doesNotHaveIs(TOP_LEFT)
    val bottom      = theOneWireIn(nine - top.wire).that(four).doesNotHaveIs(BOTTOM)
    val bottomRight = theOneWireIn(one).thatIsNotThe(topRight.wire) to BOTTOM_RIGHT
    return mapOf(top, bottomLeft, topLeft, middle, topRight, bottomRight, bottom)
}

fun count1478digits(input: String): Int {
    val signals = input.lines().map { toSignal(it) }
    return signals.map { it.output }
        .sumOf { output -> output.count { signal -> isADigitWithUniqueSegmentCount(signal) } }
}

private fun isADigitWithUniqueSegmentCount(signal: String): Boolean {
    val length = signal.length
    return length == 2 || length == 3 || length == 4 || length == 7
}

fun toSignal(line: String): Signal {
    val (input, output) = line.split(" | ")
    return Signal(input.split(' '), output.split(' '))
}

data class Signal(val input: List<String>, val output: List<String>)

private fun String.containsAllSegmentsOf(digit: String): Boolean {
    return digit.all { this.contains(it) }
}

private operator fun String.minus(char: Wire): String {
    return this.filter { it != char }
}

private val Pair<Wire, Segment>.wire: Wire
    get() {
        return this.first
    }

typealias Wire = Char
