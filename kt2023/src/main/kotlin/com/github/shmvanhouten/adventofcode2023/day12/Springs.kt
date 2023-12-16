package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.pairs.mapSecond
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.invoke

fun possibleArrangements(line: String): Long {
    val (springs, records) = line.splitIntoTwo(" ").mapSecond { it.split(',').map(String::toInt) }

    val permute = permute(State(springs, records))
    return getSuccessfulArrangements(permute)
}

suspend fun sumPossibleArrangements(input: String): Long = Dispatchers.Default {
        input.lines().map {
            async {
                possibleArrangements(it, 5)
            }
        }.awaitAll().sum()
    }

fun possibleArrangements(line: String, times: Int): Long {
    val (springs, records) = line.splitIntoTwo(" ")
        .mapSecond { r -> List(times) {r}.flatMap { it.split(',') }.map(String::toInt) }

    val (arrangements, _) = generateSequence(
        permute(State(springs, records)) to mutableMapOf<State, Map<Arrangement, Long>>()
    ) { (arrangements, memory) ->
        arrangements.map { (arrangement, timesReturned) ->
            val state = State(arrangement.nextChar + springs, arrangement.remainingRecords)
            memory.getOrPut(state) { permute(state) }
                .mapValues { it.value * timesReturned }
        }.combine() to memory
    }.take(times).last()

    return getSuccessfulArrangements(arrangements)
}

private fun getSuccessfulArrangements(permute: Map<Arrangement, Long>): Long {
    return permute.entries.filter { it.key.remainingRecords.isEmpty() }.sumOf { it.value }
}

private fun permute(startingState: State): Map<Arrangement, Long> {
    val states = mutableListOf(startingState)
    val counts = mutableMapOf<Arrangement, Long>()
    while (states.isNotEmpty()) {
        val state = states.removeLast()
        val (remaining, records, processedString) = state

        if (records.isEmpty()) {
            if (!remaining.contains('#')) {
                counts.incForKey(Arrangement('?'))
            }
        }

        else if(state.tailSpringCount == records.first()) {
            if(remaining.isEmpty()) {
                counts.incForKey(Arrangement('.', records.tail()))
            } else if(remaining.first() != '#') {
                states += state.processRecord()
            }
        }

        else if (remaining.isEmpty()) {
            if (processedString.last() == '#' && records.first() > state.tailSpringCount) {
                counts.incForKey(Arrangement(
                    nextChar = '#',
                    remainingRecords = listOf(records.first() - state.tailSpringCount) + records.tail()
                ))
            }
            else if (processedString.last() == '.') {
                counts.incForKey(Arrangement('?', records))
            }
        }

        else if (remaining.first() == '.') {
            if (processedString.canBeFollowedByAnyChar()) {
                states += state.process('.')
            }
        }

        else if (remaining.first() == '?' && processedString.canBeFollowedByAnyChar()) {
            states += state.process('#')
            states += state.process('.')
        }

        else {
            states += state.process('#')
        }
    }

    return counts
}

private fun List<Map<Arrangement, Long>>.combine(): Map<Arrangement, Long> = this
    .flatMap { it.asSequence() }
    .groupBy({ it.key }, { it.value })
    .mapValues { it.value.sum() }

private fun String.canBeFollowedByAnyChar(): Boolean = this.isEmpty() || this.last() == '.'

data class Arrangement(val nextChar: Char, val remainingRecords: List<Int> = emptyList())

data class State(
    val remaining: String,
    val records: List<Int>,
    val processedString: String = "",
    val processedRecords: String = ""
) {
    val tailSpringCount: Int by lazy { processedString.reversed().takeWhile { it == '#' }.count() }

    fun process(c: Char): State = copy(remaining = remaining.substring(1), processedString = processedString + c)
    fun processRecord(): State = copy(
        remaining = remaining.substring(1),
        records = records.tail(),
        processedString = processedString + '.',
        processedRecords = processedRecords + records.first()
    )
}

fun <T> List<T>.tail() = this.subList(1, this.size)

private fun MutableMap<Arrangement, Long>.incForKey(key: Arrangement) = merge(key, 1, Long::plus)
