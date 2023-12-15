package com.github.shmvanhouten.adventofcode2023.day15

fun hash(input: String): Int {
    return input.map { it.code }
        .fold(0){acc, n ->
            (acc + n) * 17 % 256
        }
}

fun putInBoxes(instructions: List<String>): List<List<Lens>> {
    val result = MutableList(256){ mutableListOf<Lens>() }
    instructions.map { toStep(it) }
        .forEach { step ->
            val (label, instruction, hash) = step
            when (instruction) {
                Action.REMOVE -> result[hash].removeIf { it.label == label }
                Action.REPLACE -> result[hash].replaceOrAppend(step.asLens())
            }
        }
    return result
}

fun calculateFocalPower(boxes: List<List<Lens>>) = boxes.mapIndexed { boxIndex, box ->
    box.mapIndexed { slot, lens ->
        (boxIndex + 1) * (slot + 1) * lens.focalLength
    }.sum()
}.sum()

private fun MutableList<Lens>.replaceOrAppend(lens: Lens) {
    val labelIndex = this.indexOfFirst { it.label == lens.label }
    if(labelIndex == -1) this.add(lens)
    else this[labelIndex] = lens
}

private fun toStep(it: String): Step {
    val label = it.takeWhile { it.isLetter() }
    return Step(
        label,
        toAction(it.first { !it.isLetter() }),
        hash(label),
        it
    )
}

private fun toAction(action: Char): Action =
    if(action == '-') Action.REMOVE
    else Action.REPLACE

data class Step(val label: String, val type: Action, val hash: Int, val asString: String) {
    fun asLens(): Lens = Lens(label, asString.last().digitToInt())

    override fun toString(): String {
        return asString
    }
}

data class Lens(val label: String, val focalLength: Int)

enum class Action {
    REMOVE,
    REPLACE
}