package com.github.shmvanhouten.adventofcode2023.day19

import com.github.shmvanhouten.adventofcode.utility.collectors.productOf
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
    return generateSequence("in") {this[it]!!.applyTo(part)}
        .dropWhile { it != "A" && it != "R" }.first() == "A"
}

fun permuteThrough(range: RangePart, workFlows: Map<String, String>, workFlow: String): List<RangePart> {
    if(workFlow == "A") return listOf(range.copy(accepted = true))
    if(workFlow == "R") return listOf(range.copy(accepted = false))
    if(workFlow.all { it.isLetter() }) return permuteThrough(range, workFlows, workFlows[workFlow]!!)

    val rangeId = workFlow.first()
    val op = workFlow[1]
    val compare = workFlow.substringBefore(':').filter { it.isDigit() }.toInt()
    val firstPermute = permuteThrough(range.withMatching(rangeId, op, compare), workFlows, workFlow.substringBetween(":", ","))
    val secondPermute = permuteThrough(range.withNotMatching(rangeId, op, compare), workFlows, workFlow.substringAfter(','))
    return firstPermute + secondPermute
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

data class RangePart(val ratings: Map<Char, IntRange>, val accepted: Boolean? = null) {
    fun withMatching(rangeId: Char, op: Char, compare: Int): RangePart {
        val intRange = ratings[rangeId]!!
        if(compare !in intRange) println("$compare not in $intRange!!!! $rangeId $op $this")
        return when (op) {
            '>' -> RangePart(ratings + (rangeId to (compare + 1)..intRange.last))
            '<' -> RangePart(ratings + (rangeId to intRange.first..<(compare)))
            else -> error("unknown op $op")
        }
    }

    fun withNotMatching(rangeId: Char, op: Char, compare: Int): RangePart {
        val intRange = ratings[rangeId]!!
        if(compare !in intRange) println("$compare not in $intRange!!!! $rangeId $op $this")
        return when (op) {
            '<' -> RangePart(ratings + (rangeId to compare..intRange.last))
            '>' -> RangePart(ratings + (rangeId to intRange.first..(compare)))
            else -> error("unknown op $op")
        }
    }

    fun count(): Long {
        return ratings.values.productOf { it.count() }
    }
}

fun startingRange() = RangePart(mapOf(
    'x' to 1..4000,
    'm' to 1..4000,
    'a' to 1..4000,
    's' to 1..4000
))

fun parse(raw: String): Pair<Map<String, WorkFlow>, List<Part>> {
    val (workFlows, parts) = raw.blocks().map { it.lines() }
    return toWorkFlows(workFlows).associateBy { it.id } to parts.map { toPart(it) }
}

fun toWorkFlows(workFlows: List<String>) = workFlows.map { toWorkFlow(it) }
