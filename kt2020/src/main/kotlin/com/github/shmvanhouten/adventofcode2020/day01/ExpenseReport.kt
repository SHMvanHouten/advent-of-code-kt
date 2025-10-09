package com.github.shmvanhouten.adventofcode2020.day01

fun findTwoNumbersThatAddUpTo2020(input: String): Pair<Int, Int> {
    return findTwoNumbersThatAddUpTo2020(input.lines().map { it.toInt() })
}

private fun findTwoNumbersThatAddUpTo2020(input: List<Int>): Pair<Int, Int> {
    return input.asSequence().flatMapIndexed { i, n ->
        input.subList(i + 1, input.size).map {
            n to it
        }
    }.first { (a, b) -> a + b == 2020 }
}

fun findThreeNumbersThatAddUpTo2020(input: String): Triple<Int, Int, Int> {
    return findThreeNumbersThatAddUpTo2020(input.lines().map { it.toInt() })
}

private fun findThreeNumbersThatAddUpTo2020(input: List<Int>): Triple<Int, Int, Int> {
    return input.asSequence().flatMapIndexed { i, n ->
        input.subList(i + 1, input.size).flatMapIndexed { i2, n2 ->
            input.subList(i + i2, input.size).map {
                Triple(n, n2, it)
            }
        }
    }.first { (a, b, c) -> a + b + c == 2020 }
}