package com.github.shmvanhouten.adventofcode2021.day24

import com.github.shmvanhouten.adventofcode2021.day24.FunctionType.A
import com.github.shmvanhouten.adventofcode2021.day24.FunctionType.B

private val resultsOfInputAtIndex = mutableMapOf<Triple<Int, Long, Int>, Long>()

fun printFirstPossibilities() {
    val (type, r, s) = parameters[0]
    (1..9).map { it to type.function.invoke(it, r, s, 0L) }
        .forEach { (previousDigit, result) ->
            val (type1, r1, s1) = parameters[1]
            (1..9).map { it to type1.function.invoke(it, r1, s1, result) }
                .forEach { (nextDigit, nextResult) ->
                    println("$previousDigit, $result -> $nextDigit, $nextResult")
                }
        }

}

// bruteforce
fun countDownUntilValid(): String {
    val start = "99999999999999"
    return generateSequence(start) { nr -> nr.toBigDecimal().dec().toString()}
        .filter { !it.contains('0') }
        .map { nr ->
        nr to nr.foldIndexed(0L) { index, previousResult, c ->
            val w = c.digitToInt()
            resultsOfInputAtIndex[Triple(index, previousResult, w)]
                ?: invokeFunction(parameters[index], w, previousResult, index)
        }
//    }.map {
//        println(it)
//        it
    }
        .take(10)
        .first { it.second == 0L }
        .first
}

private fun invokeFunction(
    digitHandlingFunction: DigitHandlingFunction,
    w: Int,
    nr: Long,
    index: Int
): Long {
    val (type, r, s) = digitHandlingFunction
    val result = type.function.invoke(w, r, s, nr)
    resultsOfInputAtIndex[Triple(index, nr, w)] = result
    return result
}

private val parameters = listOf(
    DigitHandlingFunction(A, 10, 9),
    DigitHandlingFunction(A, 12, 6),
    DigitHandlingFunction(A, 13, 4),
    DigitHandlingFunction(A, 13, 2),
    DigitHandlingFunction(A, 14, 9),
    // up to here its just (z * 26) + w + s

    // for every z where wInput = z % 26 -2
    // the previous output is divided
    // this needs to be true for _all_ B functions in order for the last function to
    // return 0
    DigitHandlingFunction(B, -2, 1),
    DigitHandlingFunction(A, 11, 10),
    DigitHandlingFunction(B, -15, 6),
    DigitHandlingFunction(B, -10, 4),
    // z = (result - s - w) / 26
    // (36645 - 4 - w) / 26 =
    // (36645 - (s = 6 + w = 5)) % 26 = 0, so w _has_ to be 5
    DigitHandlingFunction(A, 10, 6),
    // 1409 * 26 36.634 - (- 10 + 1)         36645
    // (f11 * 26) - 10 - w10
    DigitHandlingFunction(B, -10, 3),
    // target = 54 * 26 1404 - (4 + 1) = 1409
    // f11 = target = ((((w13 + 1) * 26) - 1 -w12) * 26) - 10 - w11
    DigitHandlingFunction(B, -4, 9),
    // at index 12 to attain target 2 with input 1 we need 54
    // at index 12 to attain target 3 with input 1 we need 80
    // at index 12 to attain target 4 with input 1 we need 106
    //       at index 12 to attain target 4 with input 2 we need 107
    // at index 12 to attain target 10 with input 1 we need 262
    // target = 2 * 26 - -1 - -1
    // target = 3 * 26 + 2
    // target = (((w13 + 1) * 26) - 1 - w12)
    DigitHandlingFunction(B, -1, 15),
    // in order to return 0 z needs to be between 2 and 10
    // f(w13 + 1)
    DigitHandlingFunction(B, -1, 5),
)

fun handleDigitTypeA(digitW: Int, r: Int, s: Int, z: Long ): Long {
//    val x = doComparison(digitW, z, r)  : not necessary
    val x = 1
    val y = 25 * x + 1
    val newZ = z * y
    // if(digitW == ((z % 26) + r).toInt()) z *= 26 else z = z
    val yy = (digitW + s) * x
    return newZ + yy
}
// result = z * 26 + s + w
// z = (result - s - w) / 26


fun handleDigitTypeB(digitW: Int, r: Int, s: Int, z: Long = 0): Long {
    val x = doComparison(digitW, z, r)
    val y = 25 * x + 1
    val dividedZ = z / 26
    val newZ = dividedZ * y
    val yy = (digitW + s) * x
    return newZ + yy
}
// 1 - 9 (==) z%26 + 10
private fun doComparison(digitW: Int, z: Long, r: Int) = if (digitW == ((z % 26) + r).toInt()) 0
else 1
// careful: switched 1 and 0 and != and ==

enum class FunctionType(val function: (Int, Int, Int, Long) -> Long) {
    A(::handleDigitTypeA),
    B(::handleDigitTypeB)
}

private data class DigitHandlingFunction(val type: FunctionType, val r: Int, val s: Int)



// musings
fun listNecessaryDigitsToAttainTarget(target: Long, i: Int): List<Long> {
    return (1..9).map { digitNecessaryFor(i, target, it) }
}

private fun digitNecessaryFor(index: Int, target: Long, w: Int): Long {
    val (type, r, s) = parameters[index]
    val function = type.function
    val range = if(type == B) 0..Long.MAX_VALUE
    else Long.MAX_VALUE.downTo(0)
    val necessary = range
        .asSequence()
        .map { it to function.invoke(w, r, s, it) }
        .filter { it.second == target }
        .map { it.first }
        .first()
    println("at index $index to attain target $target with input $w we need $necessary")
    return necessary
}