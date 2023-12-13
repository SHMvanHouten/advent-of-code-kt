package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.collectors.product
import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Long {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = permute(springs, records)
    println("$springs, $records:    count: ${permute}")
    return getSuccessfulArrangements(permute)
}

fun getSuccessfulArrangements(permute: Map<Pair<List<Int>, Char>, Long>): Long {
    return permute.entries.filter { it.key.first.isEmpty() }.sumOf { it.value }
}

fun possibleArrangements(line: String, times: Int): Long {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = sequence { while (true) yield(_records) }
        .take(times)
        .joinToString(",").split(',').map { it.toInt() }

    var remainingRecordsWithPermutations = permute(springs, records)
    repeat(times - 1) {
        val flatMap = remainingRecordsWithPermutations.entries.map {
            permute(it.key.second + springs, it.key.first, it.value)
        }.flatMap { it.asSequence() }
        remainingRecordsWithPermutations = flatMap
            .groupBy( { it.key }, {it.value} )
            .mapValues { it.value.sum() }
    }

    return getSuccessfulArrangements(remainingRecordsWithPermutations)
}

private fun permute(string: String, _records: List<Int>, multiplier: Long = 1): Map<Pair<List<Int>, Char>, Long> {
    val states = mutableListOf<State>(State(string, _records))
    var counts = mutableMapOf<Pair<List<Int>, Char>, Long>()
    while (states.isNotEmpty()) {
        val (remaining, records, processedString, processedRecords) = states.removeLast()
        if(records.isEmpty()) {
            if(!remaining.contains('#')) {
                counts.merge(emptyList<Int>() to '?', 1 * multiplier, Long::plus)
            }
            continue
        }
        if(remaining.isEmpty()) {
            val springCountAtEnd = getSpringCountAtEnd(processedString)
            if(records.first() == springCountAtEnd) {
                counts.merge(records.tail() to '.', 1 * multiplier, Long::plus)
            } else if(processedString.last() == '#' && records.first() > springCountAtEnd) {
                counts.merge((listOf(records.first() - springCountAtEnd) + records.tail()) to '#', 1 * multiplier, Long::plus)
            } else if(records.first() < springCountAtEnd) error("at the end spring count should not be greater than record")
            else if(processedString.last() == '.') {
                counts.merge(records to '?', 1 * multiplier, Long::plus)
            }
            continue
        }
        val springCountAtEnd = getSpringCountAtEnd(processedString)

        val firstChar = remaining.first()
        if(firstChar == '.') {
            if(processedString.isEmpty() || processedString.last() == '.') {
                states += State(remaining.substring(1), records, processedString + '.')
                continue
            } else if(springCountAtEnd == records.first()) {
                states += State(remaining.substring(1), records.tail(), processedString + '.', processedRecords + records.first())
                continue
            } else continue
        }

        if(firstChar == '#') {
            if(springCountAtEnd == records.first()) {
                continue
            }
            states += State(remaining.substring(1), records, processedString + '#')
            continue
        }

        if(processedString.isEmpty()) {
            states += State(remaining.substring(1), records, "#")
            states += State(remaining.substring(1), records, ".")
            continue
        } else if(processedString.last() == '.') {
            states += State(remaining.substring(1), records, processedString + '#')
            states += State(remaining.substring(1), records, processedString + '.')
            continue
        } else if(springCountAtEnd == records.first()){
            states += State(remaining.substring(1), records.tail(), processedString + '.', processedRecords + records.first())
            continue
        }
        states += State(remaining.substring(1), records, processedString + '#')
        continue
    }

    return counts
}

private fun getSpringCountAtEnd(processedString: String) = processedString.reversed().takeWhile { it == '#' }.count()

data class State(val remaining: String, val records: List<Int>, val processedString: String = "", val processedRecords: String = "")

fun <T> List<T>.tail() = this.subList(1, this.size)
