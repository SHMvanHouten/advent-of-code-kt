package com.github.shmvanhouten.adventofcode2023.day20

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

fun main() {
    readFile("/input-day20.txt")
        .lines()
        .onEach(::println)
}

class Machine(input: String) {
    val modules = parse(input).also(::init)

    private fun init(modules: Map<String, Module1>) {
        val conjunctions = modules.values.filterIsInstance<Conjunction>()
        conjunctions.forEach { conjunction ->
            val inputs = modules.values.filter { mod -> mod.targets.contains(conjunction.id) }.map { it.id }
            conjunction.init(inputs)
        }
    }

    var lowCount = 0
    var highCount = 0
    var starts = 0

    fun button(): Boolean {
        starts++
//        printState()
//        modules["xl"]!!.also { println("${it.id}: ${(it as FlipFlop).state}") }
        lowCount++
        val broadcaster = modules["broadcaster"]!!
        var pulses = broadcaster.input(false, starts = starts)
        while (pulses.isNotEmpty()) {
            pulses = pulses.onEach {pulse ->
                if(pulse.dest == "rx" && !pulse.pulse) return true
                if(pulse.pulse) highCount++ else lowCount++
            }.mapNotNull { pulse ->
                val module = modules[pulse.dest]
                module?.input(pulse.pulse, pulse.source, starts)
            }.flatten()
        }
        return false
    }

    private fun printState() {
        println()
        modules.values.filterIsInstance<Conjunction>().joinToString("") { if (it.state()) "1${it.id}" else "0${it.id}" }
            .also(::print)
        print(" - ")
        modules.values.filterIsInstance<FlipFlop>().joinToString("") { if (it.state) "1${it.id}" else "0${it.id}" }
            .also(::print)
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
    val type: String
    val id: String
    val targets: List<String>
    fun input(pulse: Boolean, sender: String = "", starts: Int): List<Pulse>
}

data class Broadcaster(override val id: String, override val targets: List<String>): Module1 {
    override val type = "#"
    override fun input(pulse: Boolean, sender: String, starts: Int): List<Pulse>{
        return targets.map { Pulse(pulse, id, it) }
    }
}

data class FlipFlop(override val id: String, override val targets: List<String>): Module1 {
    override val type = "%"
    var state: Boolean = false

    override fun input(pulse: Boolean, sender: String, starts: Int): List<Pulse> {
        return if(pulse) emptyList()
        else {
            state = !state
            targets.map { Pulse(state, id, it) }
        }
    }
}

data class Conjunction(override val id: String, override val targets: List<String>): Module1 {
    override val type = "&"

    private val memory= mutableMapOf<String, Boolean>()
    override fun input(pulse: Boolean, sender: String, starts: Int): List<Pulse> {
        memory[sender] = pulse
        return send(starts)
    }

    private fun send(starts: Int): List<Pulse> {
        val result = state()
        if(id == "ql" && memory.values.count { it } >= 2) println("$id at $starts: $memory")
        return targets.map { Pulse(result, id, it) }
    }

    fun init(inputs: List<String>) {
        memory += inputs.map { it to false }
    }

    fun state(): Boolean {
        return !memory.values.all { it }
    }
}

data class Pulse(val pulse: Boolean, val source: String, val dest: String)
