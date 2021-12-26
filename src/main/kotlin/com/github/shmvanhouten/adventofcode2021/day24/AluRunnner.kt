package com.github.shmvanhouten.adventofcode2021.day24

class AluRunnner(private val input: String) {
    private val instructions = input.lines().map { it.split(' ') }
    private val variables = mutableMapOf(
        "w" to 0L,
        "x" to 0,
        "y" to 0,
        "z" to 0,
    )

    fun check(nr: String): Long {
        variables.reset()
        val number = nr.map { it.digitToInt() }
        var wPointer = 0
        instructions.forEach { instruction ->
            val operator = instruction[0]
            val p1 = instruction[1]
            val p2 = if(instruction.size == 3) instruction[2]
                else "-1"
            val amount: Long = if (variables.contains(p2)) variables[p2]!!
                else p2.toLong()
            when(operator) {
                "inp" -> {
                    println("z at $wPointer is ${variables["z"]}")
                    variables["w"] = number[wPointer++].toLong()
                }
                "add" -> variables[p1] = variables[p1]!! + amount
                "mul" -> variables[p1] = variables[p1]!! * amount
                "div" -> variables[p1] = variables[p1]!! / amount
                "mod" -> variables[p1] = variables[p1]!! % amount
                "eql" -> {
                    variables[p1] = if(variables[p1]!! == amount.toLong()) 1
                        else 0
                }
            }
        }
        val z = variables["z"]!!
        return z
    }

    fun runSingleStep(w: Long = 0L, z: Long = 0L, index: Int): Long {
        val indicesOfInputInstructions = listIndicesOfInputInstructions()
        return -1
    }

    private fun listIndicesOfInputInstructions() = instructions
        .mapIndexed { index, instruction -> index to instruction }
        .filter { it.second[0] == "inp" }
        .map { it.first }

}

private fun MutableMap<String, Long>.reset() {
    this.entries.forEach{ (k, _) ->
        this[k] = 0
    }
}
