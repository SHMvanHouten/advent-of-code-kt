package com.github.shmvanhouten.adventofcode2022.day13

import com.github.shmvanhouten.adventofcode.utility.strings.blocks

fun findIndicesOfCorrectPacketPairs(input: String): List<Int> {
    return input.blocks().mapIndexedNotNull { i, pair ->
        if(orderedCorrectly(pair.lines())) i + 1
        else null
    }
}

fun sortedWithDividers(input: String): List<String> {
    return input.lines()
        .filter { it.isNotBlank() }
        .plus("[[2]]")
        .plus("[[6]]")
        .sortedWith { l, r -> compare(r, l) }
}

fun List<String>.decoderKey(): Int {
    return (this.indexOf("[[2]]") + 1) * (this.indexOf("[[6]]") + 1)
}

private fun orderedCorrectly(pairs: List<String>): Boolean {
    return orderedCorrectly(pairs[0], pairs[1])
}

fun orderedCorrectly(l: String, r: String): Boolean {
    return when(compare(l, r)) {
        -1 -> false
        else -> true
    }
}

fun compare(left: String, right: String): Int {
    if (left.isEmpty() && right.isEmpty()) return 0
    if (left.isEmpty()) return 1
    if (right.isEmpty()) return -1
    if (left.first() == '[' && right.first() != '[') return compare(left, right.addBracketsAroundFirst())
    if (left.first() != '[' && right.first() == '[') return compare(left.addBracketsAroundFirst(), right)
    if(left.first() == '[' && right.first() == '[') {
        val leftClosingBracketIndex = left.findClosingBracketIndex()
        val rightClosingBracketIndex = right.findClosingBracketIndex()
        val compare = compare(
            left.substring(1, leftClosingBracketIndex),
            right.substring(1, rightClosingBracketIndex)
        )
        if(compare == 0) return compare(left.stringAfter(leftClosingBracketIndex), right.stringAfter(rightClosingBracketIndex))
        else return compare
    }
    val leftNr = left.substringBefore(',').toInt()
    val rightNr = right.substringBefore(',').toInt()
    if (leftNr < rightNr) return 1
    if (leftNr > rightNr) return -1
    if(!left.contains(',') && !right.contains(',')) return 0
    return compare(left.substringAfter(',', ""), right.substringAfter(',', ""))

}

private fun String.stringAfter(index: Int): String {
    if(index == this.lastIndex) return ""
    else return this.substring(index + 2) // 2 chars: ],
}

private fun String.addBracketsAroundFirst(): String {
    if(this.all { it.isDigit() }) return "[$this]"
    val afterNr = this.indexOfFirst { !it.isDigit() }
    val nrWithBrackets = "[${this.substring(0, afterNr)}]"
    return if(afterNr >= this.lastIndex) nrWithBrackets
    else "$nrWithBrackets${this.substring(afterNr)}"
}

private fun String.findClosingBracketIndex(): Int {
    var openingBracketCount = 0
    var closingBracketCount = 0
    this.forEachIndexed { i, c ->
        if (c == '[') openingBracketCount++
        else if (c == ']') closingBracketCount++
        if (openingBracketCount == closingBracketCount) return i
    }
    error("no closing bracket found for $this")
}

private fun isAList(s: String) = s.first() == '['

private fun String.hasNext(): Boolean = isNotBlank()

interface Comparable {

}

class Nr(val value: Int) : Comparable {
    fun toList(): List_ {
        return List_(listOf(this))
    }
}

class List_(val value: List<Comparable>) : Comparable