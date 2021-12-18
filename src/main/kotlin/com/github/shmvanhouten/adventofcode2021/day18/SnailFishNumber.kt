package com.github.shmvanhouten.adventofcode2021.day18

interface SnailFishNumber {
    operator fun plus(other: SnailFishNumber): SnumberPair {
        return SnumberPair(this, other)
    }

    fun explode(): SnailFishNumber

    fun explode(depth: Int): Pair<SnailFishNumber, Pair<Int?, Int?>>
    fun addToLeftMost(value: Int): SnailFishNumber
    fun addToRightMost(value: Int): SnailFishNumber
    fun split(): SnailFishNumber
}

data class RegularNumber(val value: Int): SnailFishNumber {
    override fun split(): SnailFishNumber {
        return if(value >= 10) {
            SnumberPair(RegularNumber(value.floorDiv(2)), RegularNumber(value.ceilDiv(2)))
        } else this
    }

    override fun explode(): SnailFishNumber {
        return this
    }

    override fun explode(depth: Int): Pair<SnailFishNumber, Pair<Int?, Int?>> {
        val resolve = this
        return resolve to Pair(null, null)
    }

    override fun addToLeftMost(value: Int): SnailFishNumber {
        return RegularNumber(this.value + value)
    }

    override fun addToRightMost(value: Int): SnailFishNumber {
        return RegularNumber(this.value + value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

private fun Int.ceilDiv(divisor: Int): Int {
    return if(this % divisor != 0) this.div(divisor) + 1
    else this.div(divisor)
}

data class SnumberPair(val first: SnailFishNumber, val second: SnailFishNumber) : SnailFishNumber {
    override fun explode(): SnailFishNumber {
        return explode(0).first
    }

    override fun split(): SnailFishNumber {
        val firstSplit = first.split()
        if(firstSplit != first) return this.copy(first = firstSplit)
        val secondSplit = second.split()
        if(secondSplit != second) return this.copy(second = secondSplit)
        else return this
    }

    override fun explode(depth: Int): Pair<SnailFishNumber, Pair<Int?, Int?>> {
        if (depth >= 3) {
            return if(first is RegularNumber && second is RegularNumber) {
                this to Pair(null, null)
            } else if (first is SnumberPair) {
                val (val1, val2) = first.explodeValues()
                SnumberPair(RegularNumber(0), second.addToLeftMost(val2)) to Pair(val1, null)
            } else if(second is SnumberPair) {
                val (val1, val2) = second.explodeValues()
                SnumberPair(first.addToRightMost(val1), RegularNumber(0)) to Pair(null, val2)
            } else error("unexpected state at depth $depth, first: $first, second: $second")

        } else {
            val (firstSnailFishResult, firstRest) = first.explode(depth + 1)

            if (firstRest.first != null) {
                return this.copy(first = firstSnailFishResult) to firstRest
            } else if (firstRest.second != null) {
                return SnumberPair(first = firstSnailFishResult, second = second.addToLeftMost(firstRest.second!!)) to Pair(null, 0)
            }

            val (secondSnailFishResult, secondRest) = second.explode(depth + 1)
            return if(secondRest.first != null) {
                SnumberPair(first = first.addToRightMost(secondRest.first!!), second = secondSnailFishResult) to Pair(0, null)
            } else if(secondRest.second != null) {
                this.copy(second = secondSnailFishResult) to secondRest
            }else {
                SnumberPair(firstSnailFishResult, secondSnailFishResult) to Pair(null, null)
            }
        }
    }

    private fun explodeValues(): Pair<Int, Int> {
        if(this.first !is RegularNumber || this.second !is RegularNumber) error("durp")
        return first.value to second.value
    }

    override fun addToLeftMost(value: Int): SnailFishNumber {
        return this.copy(first = first.addToLeftMost(value))
    }

    override fun addToRightMost(value: Int): SnailFishNumber {
        return this.copy(second = second.addToRightMost(value))
    }

    override fun toString(): String {
        return "[$first,$second]"
    }
}

fun parseSnailFishNumber(input: String): List<SnailFishNumber> {
    return input.lines().map { toSnailFishNumber(it) }
}

fun toSnailFishNumber(line: String): SnailFishNumber {
    return evaluateFishNumber(line.substring(1, line.lastIndex))
}

fun evaluateFishNumber(line: String): SnailFishNumber {
    if(line.first() != '[' && line.last() != ']') {
        val (n1, n2) = line.split(',')
        return SnumberPair(RegularNumber(n1.toInt()), RegularNumber(n2.toInt()))

    } else if(line.first() != '[') {
        return SnumberPair(
            RegularNumber(line.substring(0, line.indexOf(',')).toInt()),
            toSnailFishNumber(line.substring(line.indexOf(',') + 1))
        )

    } else if(line.last() != ']') {
        return SnumberPair(
            toSnailFishNumber(line.substring(0, line.lastIndexOf(','))),
            RegularNumber(line.substring(line.lastIndexOf(',') + 1).toInt())
        )

    } else {
        val splitIndex = findIndexOfCommaAfterClosingBracketThatMatchesOpeningBracket(line)
        return SnumberPair(
            toSnailFishNumber(line.substring(0, splitIndex)),
            toSnailFishNumber(line.substring(splitIndex + 1))
        )
    }
}

fun findIndexOfCommaAfterClosingBracketThatMatchesOpeningBracket(line: String): Int {
    var nrOfOpeningBrackets = 0
    for (index in line.indices) {
        val c = line[index]
        if (c == '[') nrOfOpeningBrackets++
        else if(c == ']') nrOfOpeningBrackets--
        if(c == ',' && nrOfOpeningBrackets == 0) return index
    }
    error("comma not found in $line")
}
