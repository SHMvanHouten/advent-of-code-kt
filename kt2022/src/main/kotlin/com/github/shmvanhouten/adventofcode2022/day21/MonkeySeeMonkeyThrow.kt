package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun shout(example: String): Long {
    val monkeys = example.lines().map { toMonkey(it) }.associateBy { it.name }
    return monkeys["root"]!!.getNumber(monkeys)
}

fun toMonkey(line: String): Monkey {
    val words = line.words()
    val name = words.first().substringBefore(':')
    return if (words.size == 2) NumberMonkey(name, words[1].toLong())
    else {
        OperationMonkey(
            name,
            words[2].toOperation(),
            words[1],
            words[3]
        )
    }
}

private fun String.toOperation(): (Long, Long) -> Long {
    return when(this) {
        "+" -> {
            {one, other -> one + other}
        }
        "-" -> {one, other -> one - other}
        "*" -> {one, other -> one * other}
        "/" -> {one, other -> one / other}
        else -> error("unknow operation: $this")
    }
}

sealed interface Monkey {
    fun getNumber(monkeys: Map<String, Monkey>): Long {
        if(this is NumberMonkey) return this.number
        this as OperationMonkey
        return operation.invoke(
            monkeys[this.monkey1]!!.getNumber(monkeys),
            monkeys[this.monkey2]!!.getNumber(monkeys)
        )
    }

    val name: String
}

data class NumberMonkey(override val name: String, val number: Long): Monkey

data class OperationMonkey(
    override val name: String,
    val operation: (Long, Long) -> Long,
    val monkey1: String,
    val monkey2: String
): Monkey