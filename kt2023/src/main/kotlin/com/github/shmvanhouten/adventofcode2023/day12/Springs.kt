package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Long {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = permute(State(springs, records))
    return getSuccessfulArrangements(permute)
}

private val previousResults = mutableMapOf<State, Map<Arrangement, Long>>()

fun possibleArrangements(line: String, times: Int): Long {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = List(times) { _records }
        .flatMap { it.split(',') }
        .map { it.toInt() }

    var remainingRecordsWithPermutations = permute(State(springs, records))
    repeat(times - 1) {
        remainingRecordsWithPermutations = remainingRecordsWithPermutations.map { (arrangement, timesReturned) ->
            val state = State(arrangement.nextChar + springs, arrangement.remainingRecords)
            val result = previousResults.getOrPut(state) { permute(state) }

            result.mapValues { it.value * timesReturned }
        }
            .flatMap { it.asSequence() }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.sum() }
    }

    return getSuccessfulArrangements(remainingRecordsWithPermutations)
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
