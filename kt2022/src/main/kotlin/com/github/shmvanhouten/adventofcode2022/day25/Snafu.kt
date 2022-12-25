package com.github.shmvanhouten.adventofcode2022.day25

private val five = (5).toBigInteger()

fun toDecimal(snafu: String): Long {
    return snafu.reversed()
        .mapIndexed { i, c -> (c.fromSnafu() * five.pow(i).toLong()) }
        .sum()
}

fun toSnafu(decimal: Long): String {

    var number = decimal.toBigInteger()
    var result = ""
    while (number.toLong() > 0) {
        val (divide, rem) = number.divideAndRemainder(five)
        val (nr, remainder) = when(rem) {
            (4).toBigInteger() -> '-' to 1
            (3).toBigInteger() -> '=' to 1
            else -> rem.toString().first() to 0
        }
        result += nr
        number = divide + remainder.toBigInteger()
    }
    return result.reversed()
}

private fun Char.fromSnafu(): Int = when(this) {
    '2' -> 2
    '1' -> 1
    '0' -> 0
    '-' -> -1
    '=' -> -2
    else -> error("not a snafu $this")
}
