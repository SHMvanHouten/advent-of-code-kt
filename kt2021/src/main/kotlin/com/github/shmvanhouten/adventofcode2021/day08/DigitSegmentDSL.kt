package com.github.shmvanhouten.adventofcode2021.day08

fun theOneWireIn(digit: String): BiggerDigit {
    return BiggerDigit(digit)
}

class BiggerDigit(private val biggerDigit: String) {
    fun that(smallerDigit: String): DigitComparer {
        return DigitComparer(biggerDigit, smallerDigit)
    }

    fun thatIsNotThe(wire: Wire): Char {
        return biggerDigit.first { it != wire }
    }
}

class DigitComparer(private val biggerDigit: String, private val smallerDigit: String) {
    fun doesNotHaveIs(segment: Segment): Pair<Wire, Segment> {
        return biggerDigit.single { !smallerDigit.contains(it) } to segment
    }
}

