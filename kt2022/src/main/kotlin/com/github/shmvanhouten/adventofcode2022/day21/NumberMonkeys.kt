package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words
import java.math.BigInteger

fun shout(input: String): Long {
    val monkeys = input.lines().map { toMonkey(it) }.associateBy { it.name }
    return monkeys["root"]!!.getNumber(monkeys)
}

internal fun toMonkey(line: String): Monkey {
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

fun String.toOperation(): (Long, Long) -> Long {
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


fun String.toReverseOperationBi(): (BigInteger, BigInteger) -> BigInteger {
    return when(this) {
        "+" -> {
            {one, other -> one - other}
        }
        "-" -> {one, other -> one + other}
        "*" -> {one, other -> one / other}
        "/" -> {one, other -> one * other}
        else -> error("unknow operation: $this")
    }
}

fun String.toReverseOperation(): (Long, Long) -> Long {
    return when(this) {
        "+" -> {
            {one, other -> one - other}
        }
        "-" -> {one, other -> one + other}
        "*" -> {one, other -> one / other}
        "/" -> {one, other -> one * other}
        else -> error("unknow operation: $this")
    }
}

sealed interface Monkey {

    fun getNumber(monkeys: Map<String, Monkey>): Long

    fun hasAHumanInChain(monkeys: Map<String, Monkey>): Boolean {
        return this.name == HUMAN
    }

    fun doReverseOperation(monkeys: Map<String, Monkey>, checkNr: Long): Long
    val name: String
}

data class NumberMonkey(override val name: String, val number: Long): Monkey {

    override fun getNumber(monkeys: Map<String, Monkey>): Long {
        return this.number
    }
    override fun doReverseOperation(monkeys: Map<String, Monkey>, checkNr: Long): Long {
        if(name == HUMAN) {
            println(checkNr)
            return checkNr
        }
        return this.number
    }
}

data class OperationMonkey(
    override val name: String,
    val operation: String,
    val monkey1: String,
    val monkey2: String
): Monkey {

    override fun getNumber(monkeys: Map<String, Monkey>): Long {
        val number1 = monkeys[this.monkey1]!!.getNumber(monkeys)
        val number2 = monkeys[this.monkey2]!!.getNumber(monkeys)
        return operation.toOperation().invoke(
            number1,
            number2
        )
    }

    override fun hasAHumanInChain(monkeys: Map<String, Monkey>): Boolean {
        return super.hasAHumanInChain(monkeys) || monkeys[monkey1]!!.hasAHumanInChain(monkeys) || monkeys[monkey2]!!.hasAHumanInChain(monkeys)
    }

    override fun doReverseOperation(monkeys: Map<String, Monkey>, checkNr: Long): Long {
        if(name == HUMAN) {
            println(checkNr)
            return checkNr
        }
        val monkey1 = monkeys[monkey1]!!
        val monkey2 = monkeys[monkey2]!!
        return if(monkey1.hasAHumanInChain(monkeys)) {
            val number = monkey2.getNumber(monkeys)
            val result = operation.toReverseOperation().invoke(number, checkNr)
            monkey1.doReverseOperation(monkeys, result)
        } else {
            val number = monkey1.getNumber(monkeys)
            val result = operation.toReverseOperation().invoke(number, checkNr)
            monkey2.doReverseOperation(monkeys, result)
        }
    }
}
