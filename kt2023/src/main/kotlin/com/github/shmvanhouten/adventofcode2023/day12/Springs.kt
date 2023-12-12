package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Long {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = permute(springs, records)
    println("$springs, $records:    count: ${permute}")
    return permute
}

fun possibleArrangements(line: String, times: Int): Long {
    val (_springs, _records) = line.splitIntoTwo(" ")
    val records = sequence { while (true) yield(_records) }
        .take(times)
        .joinToString(",").split(',').map { it.toInt() }

    val springs = sequence { while (true) yield(_springs) }
        .take(times)
        .joinToString("?")

    return permute(springs, records).also { println(it) }
}

private fun permute(string: String, _records: List<Int>): Long {
    val states = mutableListOf<State>(State(string, _records))
    var count = 0L
    while (states.isNotEmpty()) {
        val (remaining, records, processedString, processedRecords) = states.removeLast()
        if(records.isEmpty()) {
            if(remaining.contains('#')) continue
            count++
            continue
        }
        if(remaining.isEmpty()) {
            if(records.size == 1 && records.first() == processedString.reversed().takeWhile { it == '#' }.count()) {
                count++
            }
            continue
        }
        val springCountAtEnd = processedString.reversed().takeWhile { it == '#' }.count()

        if(records.first() - springCountAtEnd > remaining.length) continue

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

    return count
}

data class State(val remaining: String, val records: List<Int>, val processedString: String = "", val processedRecords: String = "")

fun <T> List<T>.tail() = this.subList(1, this.size)
