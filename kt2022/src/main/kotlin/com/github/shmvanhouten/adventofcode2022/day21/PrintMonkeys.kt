package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words
import java.math.BigInteger

internal const val HUMAN = "humn"

fun humanMonkeyValue(input: String): BigInteger {
    val printMonkeys = printMonkeys(input)
    println(printMonkeys)
    return humanValue(printMonkeys)
}

fun humanValue(printMonkeys: String): BigInteger {
    val (left, rightSide) = printMonkeys.split('=')
    var remaining: BigInteger
    var remainingEquation = left
    if (left.first().isDigit()) {
        remaining = left.toLong().toBigInteger()
        remainingEquation = rightSide
    } else {
        remaining = rightSide.toBigInteger()
    }
    while (remainingEquation.contains('(')) {
        remainingEquation = remainingEquation.substring(1, remainingEquation.lastIndex)
        if (remainingEquation.first().isLetter() || remainingEquation.last().isLetter()) {
            return getHumanValue(remainingEquation, remaining)
        }
        val firstOperation = remainingEquation.first { !it.isDigit() && !it.isLetter() && it != '(' && it != ')' }.toString()
        if ((firstOperation == "/") && remainingEquation.substringAfter(firstOperation).contains(HUMAN)) {
            val number = remainingEquation.takeWhile { it.isDigit() }.toBigInteger()
            remainingEquation = "${remainingEquation.substringAfter(firstOperation)})"
            remaining = number / remaining
        } else if (firstOperation == "-" && remainingEquation.substringAfter(firstOperation).contains(HUMAN)) {
            val number = remainingEquation.takeWhile { it.isDigit() }.toBigInteger()
            remainingEquation = remainingEquation.substringAfter("-")
            remaining = number - remaining
        } else {
            val (number, op, whatIsLeft) = getNumberOperationAndRemaining(remainingEquation)
            remainingEquation = whatIsLeft
            remaining = doOperation(op, remaining, number)
        }
    }

    return remaining
}

fun getHumanValue(remainingEquation: String, remaining: BigInteger): BigInteger {
    if (remainingEquation.first().isLetter()) {
        val (i, c) = remainingEquation.withIndex().first { !it.value.isLetter() }
        val number = remainingEquation.substring(i + 1).toLong()
        return doOperation(c.toString(), remaining, number)
    } else {
        val number = remainingEquation.takeWhile { it.isDigit() }.toLong()
        val operation = remainingEquation.first { !it.isDigit() }.toString()
        if(operation == "-") {
            return number.toBigInteger() - remaining
        } else if(operation == "/") {
            return number.toBigInteger() / remaining
        }
        return doOperation(operation, remaining, number)
    }
}

private fun doOperation(
    operation: String,
    remaining: BigInteger,
    number: Long
): BigInteger {
    return operation.toReverseOperationBi().invoke(remaining, number.toBigInteger())
}

fun getNumberOperationAndRemaining(equation: String): Triple<Long, String, String> {
    if (equation.first().isDigit()) {
        val number = equation.takeWhile { it.isDigit() }
        val operation = equation.substring(number.length, number.length + 1)
        val remaining = equation.substring(number.length + 1)
        return Triple(number.toLong(), operation, remaining)
    } else {
        val lastClosingBracket = equation.lastIndexOf(')')
        val remaining = equation.substring(0..lastClosingBracket)
        val operation = equation.substring(lastClosingBracket + 1, lastClosingBracket + 2)
        val number = equation.substring(lastClosingBracket + 2).toLong()
        return Triple(number, operation, remaining)
    }
}

fun printMonkeys(input: String): String {
    val monkeys = input.lines().map { toPrintMonkey(it) }.associateBy { it.name }
    return monkeys["root"]!!.print(monkeys)
}

fun toPrintMonkey(line: String): PrintMonkey {
    val words = line.words()
    val name = words.first().substringBefore(':')

    return if (name == "root") {
        AssertionMonkey(
            name,
            words[1],
            words[3]
        )
    } else if (name == "humn") {
        HumanMonkey(name)
    } else if (words.size == 2) NumberPrintMonkey(name, words[1])
    else {
        OperationPrintMonkey(
            name,
            words[2],
            words[1],
            words[3]
        )
    }
}

sealed interface PrintMonkey {
    val name: String
    fun print(monkeys: Map<String, PrintMonkey>): String
}

data class NumberPrintMonkey(
    override val name: String,
    val number: String
) : PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return number
    }
}

data class HumanMonkey(
    override val name: String
) : PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return name
    }
}

data class AssertionMonkey(
    override val name: String,
    val monkey1: String,
    val monkey2: String
) : PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return "${monkeys[monkey1]!!.print(monkeys)}=${monkeys[monkey2]!!.print(monkeys)}"
    }
}

data class OperationPrintMonkey(
    override val name: String,
    val operation: String,
    val monkey1: String,
    val monkey2: String
) : PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        val print1 = monkeys[monkey1]!!.print(monkeys)
        val print2 = monkeys[monkey2]!!.print(monkeys)
        if (print1.contains(HUMAN) || print2.contains(HUMAN)) return "($print1$operation$print2)"
        else return operation.toOperation().invoke(print1.toLong(), print2.toLong()).toString()
    }
}