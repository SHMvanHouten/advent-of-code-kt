package com.github.shmvanhouten.adventofcode2023.day19

import com.github.shmvanhouten.adventofcode.utility.strings.blocks
import com.github.shmvanhouten.adventofcode.utility.strings.substringBetween

fun toWorkFlow(line: String): WorkFlow {
    val id = line.substringBefore('{')
    val workFlow = line.substringBetween("{", "}")
    return WorkFlow(id, workFlow)
}

fun toPart(line: String): Part {
    val (x, m, a, s) = line.substringBetween("{", "}").split(',').map { s -> s.filter { it.isDigit() }.toInt() }
    return Part(x, m, a, s)
}

data class WorkFlow(val id: String, val workFlow: String) {
    fun applyTo(part: Part): String = workFlow.applyTo(part)
}

fun String.applyTo(part: Part): String {
    if(this.all { it.isLetter() }) return this
    val partValue = part[this.first()]
    val op = when(this[1]) {
        '<' -> {a: Int, b: Int -> a < b}
        '>' -> {a, b -> a > b}
        else -> error("unknown op $this")
    }
    val compare = this.substringBefore(':').filter { it.isDigit() }.toInt()

    return if (op(partValue, compare)) substringBetween(":", ",")
    else this.substringAfter(',').applyTo(part)
}

fun Map<String, WorkFlow>.applyTo(part: Part): Boolean {
    var r = "in"
    while (r != "A" && r != "R") {
        r = this[r]!!.applyTo(part)
    }
    return r == "A"
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(char: Char) = when(char) {
        'x' -> this.x
        'm' -> this.m
        'a' -> this.a
        's' -> this.s
        else -> error("unknown part id $char")
    }

    fun value() = x + m + a + s
}

fun parse(raw: String): Pair<Map<String, WorkFlow>, List<Part>> {
    val (workFlows, parts) = raw.blocks().map { it.lines() }
    return workFlows.map { toWorkFlow(it) }.associateBy { it.id } to parts.map { toPart(it) }
}
