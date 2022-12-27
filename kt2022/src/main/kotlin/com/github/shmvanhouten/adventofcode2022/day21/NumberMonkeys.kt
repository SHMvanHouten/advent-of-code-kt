package com.github.shmvanhouten.adventofcode2022.day21

fun shout(input: String): Long {
    val monkeys = initMonkeys(input)

    return monkeys["root"]!!.number
}

fun humanMonkeyValue2(input: String): Long {
    val monkeys = initMonkeys(input)

    val root = monkeys["root"]!! as OperationMonkey
    return if (root.leftMonkey.hasHumanInChain) {
        root.leftMonkey.doReverseOperation(root.rightMonkey.number)
    } else {
        root.rightMonkey.doReverseOperation(root.leftMonkey.number)
    }
}

sealed interface Monkey {

    val number: Long

    val hasHumanInChain: Boolean
    fun doReverseOperation(checkNr: Long): Long
    val name: String
}

data class NumberMonkey(override val name: String, override val number: Long): Monkey {

    override val hasHumanInChain: Boolean
        get() = this.name == HUMAN
    override fun doReverseOperation(checkNr: Long): Long {
        return if(name == HUMAN) checkNr
        else this.number
    }
}

data class OperationMonkey(
    override val name: String,
    val operation: String,
    val leftMonkeyName: String,
    val rightMonkeyName: String,
    var monkeys: Map<String, Monkey>? = null
): Monkey {
    val leftMonkey: Monkey by lazy { monkeys!![leftMonkeyName]!! }
    val rightMonkey: Monkey by lazy { monkeys!![rightMonkeyName]!! }

    override val hasHumanInChain: Boolean by lazy {
        this.name == HUMAN || leftMonkey.hasHumanInChain || rightMonkey.hasHumanInChain
    }

    override val number: Long by lazy {
        operation.toOperation().invoke(
            leftMonkey.number,
            rightMonkey.number
        )
    }
    override fun doReverseOperation(checkNr: Long): Long {
        if(name == HUMAN) {
            return checkNr
        }
        return if(leftMonkey.hasHumanInChain) {
            val result = operation.toReverseOperation().invoke(checkNr, rightMonkey.number)
            leftMonkey.doReverseOperation(result)
        } else {
            val result = if(operation == "/" || operation == "-") {
                operation.toOperation().invoke(leftMonkey.number, checkNr)
            } else operation.toReverseOperation().invoke(checkNr, leftMonkey.number)
            rightMonkey.doReverseOperation(result)
        }
    }
}
