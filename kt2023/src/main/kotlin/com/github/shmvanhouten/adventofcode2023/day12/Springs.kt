package com.github.shmvanhouten.adventofcode2023.day12

import com.github.shmvanhouten.adventofcode.utility.strings.splitIntoTwo

fun possibleArrangements(line: String): Int {
    val (springs, _records) = line.splitIntoTwo(" ")
    val records = _records.split(',').map { it.toInt() }

    val permute = springs.permute(records)
    return permute.size
}

private fun String.permute(records: List<Int>, processedString: String = ""): List<String> {
    if(records.isEmpty()) return listOf(processedString + this.replace('?', '.'))
    if(this.isEmpty()) return emptyList()
    if(records.first() > this.length) return emptyList()

    val firstIndex = this.indexOfFirst { it == '#' || it == '?' }
    if(firstIndex == -1) return emptyList()

    val firstPossibleBlock = this[firstIndex]
    if( firstPossibleBlock == '#') {
        if(firstIndex + records.first() > this.length) return emptyList()
        val block = this.substring(firstIndex..<firstIndex + records.first())
        if(block.any { it == '.' }) return emptyList()
        else {
            val afterBlock = this.substring(firstIndex + records.first())
            if(afterBlock.isEmpty()) {
                if(records.size == 1) return listOf(processedString + block)
                else return emptyList()
            }
            if(afterBlock.first() == '#') return emptyList()
            return afterBlock.substring(1).permute(records.tail(), processedString + block.replace('?', '#') + ".")
        }
    } else {
        val afterLastUnkown = this.substring(firstIndex + 1, this.length)
        return ('#' + afterLastUnkown).permute(records, processedString) + afterLastUnkown.permute(records, processedString + ".")
    }
}

fun <T> List<T>.tail() = this.subList(1, this.size)
