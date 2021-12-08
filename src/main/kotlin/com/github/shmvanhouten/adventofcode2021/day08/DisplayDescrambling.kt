package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode2017.util.splitIntoTwo
import com.github.shmvanhouten.adventofcode2021.day08.Segment.*

fun decipherOutput(signal: Signal): Int {
    val mapping = findWireToSegmentMappings(signal.input)
    return signal.output.map { toDigit(it, mapping) }
        .joinToString("").toInt()
}

fun toDigit(hotWires: String, mapping: Map<Char, Segment>): Int {
    return segmentToDigitMapping[hotWires.map { mapping[it]!! }.sorted()]!!
}

fun findWireToSegmentMappings(input: List<String>): Map<Char, Segment> {

    val one = input.single { it.length == 2 }
    val seven = input.single { it.length == 3 }
    val four = input.single { it.length == 4 }
    val eight = input.single { it.length == 7 }
    val zeroSixOrNine = input.filter { it.length == 6 }
    val nine = zeroSixOrNine.single { maybeNine -> four.all { maybeNine.contains(it) } }

    val zeroOrSix = zeroSixOrNine - nine


    val bottomLeftWire = eight.single { !nine.contains(it) } to BOTTOM_LEFT
    val topWire = seven.single { !one.contains(it) } to TOP

    val twoThreeOfFive = input.filter { it.length == 5 }
    val two = twoThreeOfFive.single { it.contains(bottomLeftWire.first) }

    val threeOrFive = twoThreeOfFive - two
    val three = threeOrFive.single { maybeThree -> one.all { maybeThree.contains(it) } }
    val zero = zeroOrSix.single { maybeZero -> one.all { maybeZero.contains(it) } }
    val six = (zeroOrSix - zero).single()

    val middleWire = eight.single { !zero.contains(it) } to MIDDLE
    val topLeftWire = nine.single { !three.contains(it) } to TOP_LEFT
    val topRightWire = eight.single { !six.contains(it) } to TOP_RIGHT
    val bottomRightWire = one.single { it != topRightWire.first } to BOTTOM_RIGHT
    val wiresNotBottom = listOf(topWire, bottomLeftWire, topLeftWire, middleWire, topRightWire, bottomRightWire)
        .map { it.first }
    val bottomWire = eight.single { !wiresNotBottom.contains(it) } to BOTTOM
    return mapOf(topWire, bottomLeftWire, topLeftWire, middleWire, topRightWire, bottomRightWire, bottomWire)
}


fun count1478digits(input: String): Int {
    val signals = input.lines().map { toSignal(it) }
    return signals.map { it.output }
        .sumOf { output -> output.count { signal -> isA1478(signal) } }
}

fun isA1478(signal: String): Boolean {
    val length = signal.length
    return length == 2 || length == 3 || length == 4 || length == 7
}

fun toSignal(line: String): Signal {
    val (input, output) = line.splitIntoTwo(" | ")
    return Signal(input.split(' '), output.split(' '))
}

data class Signal(val input: List<String>, val output: List<String>)