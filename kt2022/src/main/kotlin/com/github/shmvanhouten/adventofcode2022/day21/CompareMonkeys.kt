package com.github.shmvanhouten.adventofcode2022.day21


fun humanMonkeyValue2(input:String): Long {
    val monkeys = input.lines().map { toMonkey(it) }.associateBy { it.name }

    val rootMonkey = monkeys["root"]!!
    val monkey1 = monkeys[(rootMonkey as OperationMonkey).monkey1]!!
    val monkey2 = monkeys[(rootMonkey as OperationMonkey).monkey2]!!
    val (checkNr, humanChain) = getEvaluableMonkeyAndHumanChain(monkey1, monkey2, monkeys)
    return humanChain.doReverseOperation(monkeys, checkNr)
}

fun getEvaluableMonkeyAndHumanChain(monkey1: Monkey, monkey2: Monkey, monkeys: Map<String, Monkey>): Pair<Long, Monkey> {
    return if(monkey1.hasAHumanInChain(monkeys)) {
        monkey2.getNumber(monkeys) to monkey1
    } else {
        monkey1.getNumber(monkeys) to monkey2
    }

}

