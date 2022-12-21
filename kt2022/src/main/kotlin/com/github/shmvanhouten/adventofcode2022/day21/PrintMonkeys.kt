package com.github.shmvanhouten.adventofcode2022.day21

import com.github.shmvanhouten.adventofcode.utility.strings.words

fun printMonkeys(input: String): String {
    val monkeys = input.lines().map { toPrintMonkey(it) }.associateBy { it.name }
    return monkeys["root"]!!.print(monkeys)
}

private const val HUMAN = "humn"

fun toPrintMonkey(line: String ): PrintMonkey {
    val words = line.words()
    val name = words.first().substringBefore(':')

    return if(name == "root"){
        AssertionMonkey(
            name,
            words[1],
            words[3]
        )
    } else if(name == "humn") {
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

sealed interface PrintMonkey{
    val name: String
    fun print(monkeys: Map<String, PrintMonkey>): String
}

data class NumberPrintMonkey(
    override val name: String,
    val number: String
): PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return number
    }
}

data class HumanMonkey(
    override val name: String
):PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return name
    }
}

data class AssertionMonkey(
    override val name: String,
    val monkey1: String,
    val monkey2: String
):PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        return "${monkeys[monkey1]!!.print(monkeys)} = ${monkeys[monkey2]!!.print(monkeys)}"
    }
}

data class OperationPrintMonkey(
    override val name: String,
    val operation: String,
    val monkey1: String,
    val monkey2: String
):PrintMonkey {
    override fun print(monkeys: Map<String, PrintMonkey>): String {
        val print1 = monkeys[monkey1]!!.print(monkeys)
        val print2 = monkeys[monkey2]!!.print(monkeys)
        if(print1.contains(HUMAN) || print2.contains(HUMAN)) return "($print1 $operation $print2)"
        else return operation.toOperation().invoke(print1.toLong(), print2.toLong()).toString()
    }
}