package com.github.shmvanhouten.adventofcode2023.day15

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile

fun main() {
    readFile("/input-day15.txt")
        .split(',')
        .sumOf { hash(it) }.also { println(it) }
}

fun hash(input: String): Int {
    return input.map { it.code }
        .fold(0){acc, n ->
            (acc + n) * 17 % 256
        }
}

fun putInBoxes(instructions: List<String>): List<List<Step>> {
    val result = MutableList<MutableList<Step>>(256){ mutableListOf<Step>() }
    instructions.map { toStep(it) }
        .forEach { step ->
            val (label, instruction, hash) = step
            when(instruction) {
                Action.REMOVE -> result[hash].removeIf { it.label == label }
                Action.REPLACE -> {
                    val box = result[hash]
                    val labelIndex = box.indexOfFirst { it.label == label }
                    if(labelIndex == -1) {
                        box.add(step)
                    } else{
                        box[labelIndex] = step
                    }
                }
            }
        }
    return result
}

fun toStep(it: String): Step {
    val label = it.takeWhile { it.isLetter() }
    return Step(
        label,
        toAction(it.first { !it.isLetter() }),
        hash(label),
        it
    )
}

fun toAction(action: Char): Action {
    return if(action == '-') Action.REMOVE
    else Action.REPLACE
}

data class Step(val label: String, val type: Action, val hash: Int, val asString: String) {
    override fun toString(): String {
        return asString
    }
    val focalLength by lazy { asString.last().digitToInt() }
}

enum class Action {
    REMOVE,
    REPLACE
}