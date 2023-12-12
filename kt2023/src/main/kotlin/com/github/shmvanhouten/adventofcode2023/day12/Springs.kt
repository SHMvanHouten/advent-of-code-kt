package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Int {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = springs.permute(records)
    return permute.size
}

private fun String.permute(records: List<Int>, processedString: String = ""): List<String> {
    if(records.isEmpty()) {
        if(this.contains('#')) return emptyList()
        return listOf(processedString + this.replace('?', '.'))
    }
    if(this.isEmpty()) {
        if(records.size == 1 && records.first() == processedString.reversed().takeWhile { it == '#' }.count()) {
            return listOf(processedString + this)
        } else return emptyList()
    }
    val springCountAtEnd = processedString.reversed().takeWhile { it == '#' }.count()

    if(records.first() - springCountAtEnd > this.length) return emptyList()

    val firstChar = this.first()
    if(firstChar == '.') {
        if(processedString.isEmpty() || processedString.last() == '.') {
            return this.substring(1).permute(records, processedString + '.')
        } else if(springCountAtEnd == records.first()) {
            return this.substring(1).permute(records.tail(), processedString + '.')
        } else return emptyList()
    }

    if(firstChar == '#') {
        if(springCountAtEnd == records.first()) {
            return emptyList()
        }
        return this.substring(1).permute(records, processedString + '#')
    }

    return if(processedString.isEmpty()) {
        return this.substring(1).permute(records, "#") + this.substring(1).permute(records, ".")
    } else if(processedString.last() == '.') {
        this.substring(1).permute(records, processedString + '#') + this.substring(1).permute(records, processedString + '.')
    } else if(springCountAtEnd == records.first()){
        this.substring(1).permute(records.tail(), processedString + '.')
    } else {
        this.substring(1).permute(records, processedString + '#')
    }

}

fun <T> List<T>.tail() = this.subList(1, this.size)
