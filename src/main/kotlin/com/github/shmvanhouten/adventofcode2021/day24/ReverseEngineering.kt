package com.github.shmvanhouten.adventofcode2021.day24

import com.github.shmvanhouten.adventofcode2021.day24.FunctionType.*

private val viableDigits = 9.downTo(1)
fun listValidNumbers(): List<String> {
    return findValidNumbers(13, 0L, "")?: error("did not find any valid numbers")
}

fun findValidNumbers(index: Int, desiredResult: Long, numberSoFar: String): List<String>? {
    if(index == -1)
        return listOf(numberSoFar)
    val (type, r, s) = digitHandingFunctions[index]

    when (type) {
        A -> {
            return viableDigits.map { w -> w to aReverseFunction(w, s, desiredResult) }
                .singleOrNull { it.second != null }
                ?.let {
                    val (nextDigit, nextDesiredResult) = it
                    findValidNumbers(index - 1, nextDesiredResult!!, nextDigit.toString() + numberSoFar)
                } ?: null // no number divisible by 26 found for this result
        }
        B -> {
            return viableDigits.map { w -> w to bReverseFunction(w, r, s, desiredResult) }
                .mapNotNull { (nextDigit, nextDesiredResult) ->
                    findValidNumbers(index - 1, nextDesiredResult, nextDigit.toString() + numberSoFar)
                }.flatten()
        }
        else -> {
            return viableDigits.map { w -> w to startingFunction(w) }
                .mapNotNull { (nextDigit, nextDesiredResult) ->
                    findValidNumbers(index - 1, nextDesiredResult, nextDigit.toString())
                }.flatten()
        }
    }
}

fun printFirstPossibilities() {
    val (type, r, s) = digitHandingFunctions[0]
    viableDigits.map { it to type.function.invoke(it, r, s, 0L) }
        .forEach { (previousDigit, result) ->
            val (type1, r1, s1) = digitHandingFunctions[1]
            (1..9).map { it to type1.function.invoke(it, r1, s1, result) }
                .forEach { (nextDigit, nextResult) ->
                    println("$previousDigit, $result -> $nextDigit, $nextResult")
                }
        }

}

private val resultsOfInputAtIndex = mutableMapOf<Triple<Int, Long, Int>, Long>()

// bruteforce
fun countDownUntilValid(): String {
    val start = "99999999999999"
    return generateSequence(start) { nr -> nr.toBigDecimal().dec().toString()}
        .filter { !it.contains('0') }
        .map { nr ->
        nr to findOutComeOfNumber(nr)
//    }.map {
//        println(it)
//        it
    }
        .take(10)
        .first { it.second == 0L }
        .first
}

fun findOutComeOfNumber(nr: String) = nr.foldIndexed(0L) { index, previousResult, c ->
    val w = c.digitToInt()
//    println("z at $index is $previousResult")
    resultsOfInputAtIndex[Triple(index, previousResult, w)]
        ?: invokeFunction(digitHandingFunctions[index], w, previousResult, index)
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

private val digitHandingFunctions = listOf(
    DigitHandlingFunction(A, 10, 0),
    DigitHandlingFunction(A, 12, 6),
    DigitHandlingFunction(A, 13, 4),
    DigitHandlingFunction(A, 13, 2),
    DigitHandlingFunction(A, 14, 9),
    DigitHandlingFunction(B, -2, 1),
    DigitHandlingFunction(A, 11, 10),
    DigitHandlingFunction(B, -15, 6),
    DigitHandlingFunction(B, -10, 4),
    DigitHandlingFunction(A, 10, 6),
    DigitHandlingFunction(B, -10, 3),
    DigitHandlingFunction(B, -4, 9),
    DigitHandlingFunction(B, -1, 15),
    DigitHandlingFunction(START, -1, 5),
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

enum class FunctionType(
    val function: (Int, Int, Int, Long) -> Long
) {
    A(::handleDigitTypeA),
    B(::handleDigitTypeB),
    START(::handleDigitTypeB)
}

fun aReverseFunction(w: Int, s: Int, resultDesired: Long): Long? {
    // z = (result - s - w) / 26
    val l = resultDesired - s - w
    return if(l % 26 == 0L) l / 26
    else null
}

fun bReverseFunction(w: Int, r: Int, s: Int, resultDesired: Long): Long {
    // (resultDesired * 26) - r - w10
    val quickFunction = (resultDesired * 26) - r + w
//    return (0..quickFunction).step(26)
//        .asSequence()
//        .map { it to B.function.invoke(w, r, s, it) }
//        .filter { it.second == resultDesired }
//        .map { it.first }
//        .firstOrNull() ?: quickFunction
    return quickFunction
}

fun startingFunction(w: Int): Long = w + 1L

private data class DigitHandlingFunction(val type: FunctionType, val r: Int, val s: Int)



// musings
fun doReverseFunctionBAtIndex(i: Int, resultDesired: Long): List<Pair<Int, Long>> {
    val (type, r, s) = digitHandingFunctions[i]
    return (0..9).map { it to bReverseFunction(it, r, s, resultDesired) }
}


fun listNecessaryDigitsToAttainTarget(target: Long, i: Int): List<Long> {
    return (1..9).map { digitNecessaryFor(i, target, it) }
}

private fun digitNecessaryFor(index: Int, target: Long, w: Int): Long {
    val (type, r, s) = digitHandingFunctions[index]
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