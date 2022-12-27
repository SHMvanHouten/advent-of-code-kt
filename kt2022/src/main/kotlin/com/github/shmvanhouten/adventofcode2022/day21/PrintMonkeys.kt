package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words

internal const val HUMAN = "humn"

fun humanMonkeyValue(input: String): Long {
    val printMonkeys = printMonkeys(input)
    println(printMonkeys)
    return humanValue(printMonkeys)
}

fun humanValue(printMonkeys: String): Long {
    var (result: Long, equation: String) = splitEquationAndResult(printMonkeys)
    while (equation != HUMAN) {
        val (valueAfterOp, equationAfterOp) = doReverseOperation(removeBrackets(equation), result)
        result = valueAfterOp
        equation = equationAfterOp
    }

    return result
}

private fun removeBrackets(equation: String) = equation.substring(1, equation.lastIndex)

private fun doReverseOperation(remainingEquation: String, remaining: Long): Pair<Long, String> {
    val (left, operation, right) = splitAroundOperation(remainingEquation)
    return if (right.contains(HUMAN) && (operation == "/" || operation == "-")) {
        // if remaining = left (-/) x
        // x = left (-/) remaining
        operation.toOperation().invoke(left.toLong(), remaining) to right
    } else {
        val number = if (left.first().isDigit()) left.toLong() else right.toLong()
        operation.toReverseOperation().invoke(remaining, number) to if (right.first().isDigit()) left else right
    }
}

fun splitAroundOperation(equation: String): Triple<String, String, String> {
    return if (equation.first().isDigit()) {
        val number = equation.takeWhile { it.isDigit() }
        val operation = equation.first { isOperation(it) }.toString()
        val remaining = equation.substringAfter(operation)
        Triple(number, operation, remaining)
    } else {
        val (indexOfOperation, operation) = equation.withIndex().last { isOperation(it.value) }
        val remaining = equation.substring(0, indexOfOperation)
        val number = equation.substring(indexOfOperation + 1)
        Triple(remaining, operation.toString(), number)
    }
}

private fun isOperation(it: Char) = it == '+' || it == '-' || it == '*' || it == '/'

private fun splitEquationAndResult(printMonkeys: String): Pair<Long, String> {
    val (left, right) = printMonkeys.split('=')
    return if (left.first().isDigit()) {
        left.toLong() to right
    } else {
        right.toLong() to left
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