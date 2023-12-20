package com.github.shmvanhouten.adventofcode2023.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

fun main() {
    readFile("/input-day20.txt")
        .lines()
        .onEach(::println)
}

class Machine(input: String) {
    private val modules = parse(input).also(::init)

    private fun init(modules: Map<String, Module1>) {
        val conjunctions = modules.values.filterIsInstance<Conjunction>()
        conjunctions.forEach { conjunction ->
            val inputs = modules.values.filter { mod -> mod.targets.contains(conjunction.id) }.map { it.id }
            conjunction.init(inputs)
        }
    }

    var lowCount = 0
    var highCount = 0

    fun button(): Boolean {
        printState()
        lowCount++
        val broadcaster = modules["broadcaster"]!!
        var pulses = broadcaster.input(false)
        while (pulses.isNotEmpty()) {
            pulses = pulses.onEach {pulse ->
                if(pulse.dest == "rx" && !pulse.pulse) return true
                if(pulse.pulse) highCount++ else lowCount++
            }.mapNotNull { pulse ->
                val module = modules[pulse.dest]
                module?.input(pulse.pulse, pulse.source)
            }.flatten()
        }
        return false
    }

    private fun printState() {
        modules.values.filterIsInstance<Conjunction>()
    }

}

private fun parse(input: String): Map<String, Module1> {
    return input.lines().map { it.toModule() }.associateBy { it.id }
}

private fun String.toModule(): Module1 {
    val targets = substringAfter("-> ").split(", ")
    return when {
        this.startsWith("broadcaster") -> {
            Broadcaster("broadcaster", targets)
        }
        startsWith("%") -> {
            FlipFlop(substringBetween("%", " "), targets)
        }
        startsWith("&") -> {
            Conjunction(substringBetween("&", " "), targets)
        }
        else -> {
            error("unknown module $this")
        }
    }
}

sealed interface Module1 {
    val id: String
    val targets: List<String>
    fun input(pulse: Boolean, sender: String = ""): List<Pulse>
}

data class Broadcaster(override val id: String, override val targets: List<String>): Module1 {
    override fun input(pulse: Boolean, sender: String): List<Pulse>{
        return targets.map { Pulse(pulse, id, it) }
    }
}

data class FlipFlop(override val id: String, override val targets: List<String>): Module1 {
    private var state: Boolean = false

    override fun input(pulse: Boolean, sender: String): List<Pulse> {
        return if(pulse) emptyList()
        else {
            state = !state
            targets.map { Pulse(state, id, it) }
        }
    }
}

data class Conjunction(override val id: String, override val targets: List<String>): Module1 {
    private val memory= mutableMapOf<String, Boolean>()
    override fun input(pulse: Boolean, sender: String): List<Pulse> {
        memory[sender] = pulse
        return send()
    }

    fun send(): List<Pulse> {
        val result = !memory.values.all { it }
        return targets.map { Pulse(result, id, it) }
    }

    fun init(inputs: List<String>) {
        memory += inputs.map { it to false }
    }
}

data class Pulse(val pulse: Boolean, val source: String, val dest: String)
