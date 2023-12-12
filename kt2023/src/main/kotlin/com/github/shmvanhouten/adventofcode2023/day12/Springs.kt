package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Int {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = springs.permute(records)
    println("$springs, $records:    count: ${permute.count()}")
    return permute.count()
}

fun possibleArrangements(line: String, times: Int): Long {
    val (_springs, _records) = line.splitIntoTwo(" ")
    val records = sequence { while (true) yield(_records) }
        .take(times)
        .joinToString(",").split(',').map { it.toInt() }

    val springs = sequence { while (true) yield(_springs) }
        .take(times)
        .joinToString("?")

    println(springs)
    println(records)
    return springs.permute(records).count().toLong().also { println(it) }
}


private tailrec fun String.permute(records: List<Int>, processedString: String = "", processedRecords: String = ""): Sequence<String> {
    if(records.isEmpty()) {
        if(this.contains('#')) return emptySequence()
        return sequenceOf(processedString + this.replace('?', '.'))
    }
    if(this.isEmpty()) {
        if(records.size == 1 && records.first() == processedString.reversed().takeWhile { it == '#' }.count()) {
            return sequenceOf(processedString + this)
        } else return emptySequence()
    }
    val springCountAtEnd = processedString.reversed().takeWhile { it == '#' }.count()

    if(records.first() - springCountAtEnd > this.length) return emptySequence()

    val firstChar = this.first()
    if(firstChar == '.') {
        if(processedString.isEmpty() || processedString.last() == '.') {
            return this.substring(1).permute(records, processedString + '.')
        } else if(springCountAtEnd == records.first()) {
            return this.substring(1).permute(records.tail(), processedString + '.', processedRecords + records.first())
        } else return emptySequence()
    }

    if(firstChar == '#') {
        if(springCountAtEnd == records.first()) {
            return emptySequence()
        }
        return this.substring(1).permute(records, processedString + '#')
    }

    if(processedString.isEmpty()) {
        return this.substring(1).permute(records, "#") + this.substring(1).permute(records, ".")
    } else if(processedString.last() == '.') {
        return this.substring(1).permute(records, processedString + '#') + this.substring(1).permute(records, processedString + '.')
    } else if(springCountAtEnd == records.first()){
        return this.substring(1).permute(records.tail(), processedString + '.', processedRecords + records.first())
    }
    return this.substring(1).permute(records, processedString + '#')

}

fun <T> List<T>.tail() = this.subList(1, this.size)
