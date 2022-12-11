package com.github.shmvanhouten.adventofcode2022.day11

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode.utility.strings.words
import java.math.BigInteger

data class Monkey(
    val items: ArrayDeque<BigInteger>,
    val operation: (BigInteger) -> BigInteger,
    val test: (BigInteger) -> Boolean,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int,
    var timesThrown: Long = 0
) {
    fun doMonkeyBusiness(monkeys: List<Monkey>, allMonkeyValues: BigInteger) {
        val trueMonkey = monkeys[trueMonkeyIndex]
        val falseMonkey = monkeys[falseMonkeyIndex]

        for (item in items) {
            val operatedItem: BigInteger = operation(item) % allMonkeyValues
            if(test(operatedItem)) {
                trueMonkey.items.addLast(operatedItem)
            } else {
                falseMonkey.items.addLast(operatedItem)
            }
        }
        timesThrown += items.size
        items.clear()
    }
}

fun parse(input: String): List<Monkey> {
    return input.blocks().map { toMonkey(it) }
}

fun toMonkey(it: String): Monkey {
    val lines = it.lines()
    return Monkey(
        lines[1].substringAfter("items:").split(',').map { it.trim().toBigInteger() }.let { ArrayDeque(it) },
        toOperation(lines[2]),
        toTest(lines[3]),
        lines[4].trim().words()[5].toInt(),
        lines[5].trim().words()[5].toInt()
    )
}

fun toTest(line: String): (BigInteger) -> Boolean {
    // Test: divisible by 17
    return {it % line.trim().words()[3].toBigInteger() == BigInteger.ZERO }
}

fun toOperation(line: String): (BigInteger) -> BigInteger {
    val words = line.trim().words()
    val operation: (l: BigInteger, o:BigInteger) -> BigInteger = when(words[4]) {
        "+" -> {l: BigInteger, o: BigInteger -> l + o}
        "*" -> {l: BigInteger, o: BigInteger -> l * o}
        else -> error("unknown op: ${words[4]}")
    }
    return when {
        words[5] == "old" -> { it -> operation(it, it) }
        else -> {it -> operation(it, words[5].toBigInteger())}
    }

}
