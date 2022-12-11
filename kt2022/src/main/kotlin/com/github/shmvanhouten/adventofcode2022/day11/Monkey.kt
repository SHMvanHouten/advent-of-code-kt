package com.github.shmvanhouten.adventofcode2022.day11

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode.utility.strings.words

data class Monkey(
    val items: ArrayDeque<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Boolean,
    val trueMonkeyIndex: Int,
    val falseMonkeyIndex: Int,
    var timesThrown: Long = 0
) {
    fun doMonkeyBusiness(monkeys: List<Monkey>) {
        val trueMonkey = monkeys[trueMonkeyIndex]
        val falseMonkey = monkeys[falseMonkeyIndex]

        for (item in items) {
            val operatedItem: Long = operation(item) / 3L
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
        lines[1].substringAfter("items:").split(',').map { it.trim().toLong() }.let { ArrayDeque(it) },
        toOperation(lines[2]),
        toTest(lines[3]),
        lines[4].trim().words()[5].toInt(),
        lines[5].trim().words()[5].toInt()
    )
}

fun toTest(line: String): (Long) -> Boolean {
    // Test: divisible by 17
    return {it % line.trim().words()[3].toLong() == 0L}
}

fun toOperation(line: String): (Long) -> Long {
    val words = line.trim().words()
    val operation: (l: Long, o:Long) -> Long = when(words[4]) {
        "+" -> {l: Long, o: Long -> l + o}
        "*" -> {l: Long, o: Long -> l * o}
        else -> error("unknown op: ${words[4]}")
    }
    return when {
        words[5] == "old" -> { it -> operation(it, it) }
        else -> {it -> operation(it, words[5].toLong())}
    }

}
