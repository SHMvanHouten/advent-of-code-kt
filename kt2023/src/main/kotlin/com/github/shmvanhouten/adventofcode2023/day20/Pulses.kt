package com.github.shmvanhouten.adventofcode2023.day20

import com.github.shmvanhouten.adventofcode.utility.compositenumber.leastCommonMultiple
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

class Machine(input: String) {
    private val modules = parse(input).also(::init)

    var lowCount = 0
    var highCount = 0
    private var buttonPresses = 0
    private val qlSources = mutableMapOf<String, Int>()

    fun button(): Long {
        buttonPresses++
        lowCount++
        val broadcaster = modules["broadcaster"]!!
        var pulses = broadcaster.input(false)
        while (pulses.isNotEmpty()) {
            pulses = pulses.onEach { pulse ->
                // Part 2 check
                if (pulse.dest == "ql" && pulse.pulse) {
                    qlSources += pulse.source to buttonPresses
                    if (qlSources.size == 4) return leastCommonMultiple(qlSources.values.map { it.toLong() })
                }
                // End Part 2 check

                if (pulse.pulse) highCount++ else lowCount++
            }.mapNotNull { pulse -> modules[pulse.dest]?.to(pulse) }
                .flatMap { (module, pulse) -> module.input(pulse.pulse, pulse.source) }
        }
        return -1
    }

    private fun init(modules: Map<String, Module1>) {
        val conjunctions = modules.values.filterIsInstance<Conjunction>()
        conjunctions.forEach { conjunction ->
            val inputs = modules.values
                .filter { it.targets.contains(conjunction.id) }
                .map { it.id }
            conjunction.connect(inputs)
        }
    }

}

data class Pulse(val pulse: Boolean, val source: String, val dest: String)

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
        return targets.map { Pulse(!memory.values.all { it }, id, it) }
    }

    fun connect(inputs: List<String>) {
        memory += inputs.map { it to false }
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