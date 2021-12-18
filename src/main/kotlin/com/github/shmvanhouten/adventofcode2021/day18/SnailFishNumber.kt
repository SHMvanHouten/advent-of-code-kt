package com.github.shmvanhouten.adventofcode2021.day18

interface SnailFishNumber {
    operator fun plus(other: SnailFishNumber): SnumberPair {
        return SnumberPair(this, other)
    }

    fun resolve(): SnailFishNumber

    fun resolve(depth: Int): Pair<SnailFishNumber, Triple<Int?, Int?, Boolean>>
    fun addToLeftMost(value: Int): SnailFishNumber
    fun addToRightMost(value: Int): SnailFishNumber
}

data class RegularNumber(val value: Int): SnailFishNumber {
    override fun resolve(): SnailFishNumber {
        return if(value >= 10) {
            SnumberPair(RegularNumber(value.floorDiv(2)), RegularNumber(value.ceilDiv(2)))
        } else this
    }

    override fun resolve(depth: Int): Pair<SnailFishNumber, Triple<Int?, Int?, Boolean>> {
        val resolve = resolve()
        return resolve to Triple(null, null, resolve is SnumberPair)
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
    override fun resolve(): SnailFishNumber {
        return resolve(0).first
    }

    override fun resolve(depth: Int): Pair<SnailFishNumber, Triple<Int?, Int?, Boolean>> {
        if (depth >= 3) {
            return if(first is RegularNumber && second is RegularNumber) {
                val (result1, rest1) = first.resolve(depth = -1)
                if(rest1.third) return SnumberPair(result1, second) to rest1

                val (result2, rest2) = second.resolve(depth = -1)
                return if(rest2.third) SnumberPair(first, result2) to rest2
                else this to Triple(null, null, false)
            } else if (first is SnumberPair) {
                val (val1, val2) = first.explode()
                SnumberPair(RegularNumber(0), second.addToLeftMost(val2)) to Triple(val1, null, false)
            } else if(second is SnumberPair) {
                val (val1, val2) = second.explode()
                SnumberPair(first.addToRightMost(val1), RegularNumber(0)) to Triple(null, val2, false)
            } else error("unexpected state at depth $depth, first: $first, second: $second")

        } else {
            val (firstSnailFishResult, firstRest) = first.resolve(depth + 1)
            if (firstRest.third) return SnumberPair(firstSnailFishResult, second) to firstRest

            if (firstRest.first != null) {
                return this.copy(first = firstSnailFishResult) to firstRest
            } else if (firstRest.second != null) {
                return SnumberPair(first = firstSnailFishResult, second = second.addToLeftMost(firstRest.second!!)) to Triple(null, 0, false)
            }

            val (secondSnailFishResult, secondRest) = second.resolve(depth + 1)
            if (secondRest.third) {
                return SnumberPair(first, secondSnailFishResult) to secondRest
            }
            return if(secondRest.first != null) {
                SnumberPair(first = first.addToRightMost(secondRest.first!!), second = secondSnailFishResult) to Triple(0, null, false)
            } else if(secondRest.second != null) {
                this.copy(second = secondSnailFishResult) to secondRest
            }else {
                SnumberPair(firstSnailFishResult, secondSnailFishResult) to Triple(null, null, false)
            }
        }
    }

    private fun explode(): Pair<Int, Int> {
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
