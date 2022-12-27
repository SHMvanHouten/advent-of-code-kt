package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words

internal fun initMonkeys(input: String): Map<String, Monkey> {
    val monkeys = input.lines().map { toMonkey(it) }.associateBy { it.name }
    monkeys.values.filterIsInstance<OperationMonkey>().forEach { it.monkeys = monkeys }
    return monkeys
}

private fun toMonkey(line: String): Monkey {
    val words = line.words()
    val name = words.first().substringBefore(':')
    return if (words.size == 2) NumberMonkey(name, words[1].toLong())
    else {
        OperationMonkey(
            name,
            words[2],
            words[1],
            words[3]
        )
    }
}

internal fun String.toOperation(): (Long, Long) -> Long {
    return when(this) {
        "+" -> {
            {one, other -> one + other}
        }
        "-" -> {one, other -> one - other}
        "*" -> {one, other -> one * other}
        "/" -> {one, other -> one / other}
        else -> error("unknown operation: $this")
    }
}

internal fun String.toReverseOperation(): (Long, Long) -> Long {
    return when(this) {
        "+" -> {
            {one, other -> one - other}
        }
        "-" -> {one, other -> one + other}
        "*" -> {one, other -> one / other}
        "/" -> {one, other -> one * other}
        else -> error("unknown operation: $this")
    }
}