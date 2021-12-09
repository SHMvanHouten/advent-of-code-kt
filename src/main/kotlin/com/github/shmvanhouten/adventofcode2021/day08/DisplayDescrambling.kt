package com.github.shmvanhouten.adventofcode2021.day08

import com.github.shmvanhouten.adventofcode2021.day08.Segment.*

fun decipherOutput(signal: Signal): Int {
    val mapping = findWireToSegmentMappings(signal.input)
    return signal.output
        .map { digit -> digit.toInt(mapping) }
        .joinInts()
}

fun findWireToSegmentMappings(input: List<Digit>): Map<Wire, Segment> {

    val one = input.single { it.length == 2 }
    val seven = input.single { it.length == 3 }
    val four = input.single { it.length == 4 }
    val eight = input.single { it.length == 7 }

    val zeroSixAndNine = input.filter { it.length == 6 }
    val nine = zeroSixAndNine.single { it.containsAllSegmentsOf(four) }
    val six = zeroSixAndNine.single { !it.containsAllSegmentsOf(one) }
    val zero = (zeroSixAndNine - nine - six).single()

    val twoThreeAndFive = input.filter { it.length == 5 }
    val three = twoThreeAndFive.single { it.containsAllSegmentsOf(one) }

    val bottomLeft  = theOneWireIn(eight).that(nine).doesNotHaveIs(BOTTOM_LEFT)
    val topRight    = theOneWireIn(eight).that(six).doesNotHaveIs(TOP_RIGHT)
    val middle      = theOneWireIn(eight).that(zero).doesNotHaveIs(MIDDLE)
    val top         = theOneWireIn(seven).that(one).doesNotHaveIs(TOP)
    val topLeft     = theOneWireIn(nine).that(three).doesNotHaveIs(TOP_LEFT)
    val bottom      = theOneWireIn(nine - top.wire).that(four).doesNotHaveIs(BOTTOM)
    val bottomRight = theOneWireIn(one).thatIsNotThe(topRight.wire) to BOTTOM_RIGHT
    return mapOf(top, bottomLeft, topLeft, middle, topRight, bottomRight, bottom)
}

private fun Digit.toInt(wireToSegmentMapping: Map<Wire, Segment>): Int? {
    val digitAsSegments = this.map { wireToSegmentMapping[it]!! }.toSet()
    return segmentsToNumberMapping[digitAsSegments]
}

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

private fun List<Int?>.joinInts(): Int = this.joinToString("").toInt()

private typealias Wire = Char
typealias Digit = String
