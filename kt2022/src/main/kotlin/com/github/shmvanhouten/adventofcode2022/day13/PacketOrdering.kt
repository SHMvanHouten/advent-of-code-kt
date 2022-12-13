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

private fun compare(left: String, right: String): Int {
    if (!left.hasNext() && right.hasNext()) return 1
    else if (!left.hasNext()) return 0
    else if (!right.hasNext()) return -1
    if (isAList(left) && !isAList(right)) {
        val compare = compare(left, right.addBracketsAroundFirst())
        return if(compare == 0) orderedCorrectlyAfter(left, right, rightClosingBracket = right.indexOfLast { it.isDigit() })
        else  compare
    } else if (isAList(right) && !isAList(left)) {
        val compare = compare(left.addBracketsAroundFirst(), right)
        return if(compare == 0) orderedCorrectlyAfter(left, right, leftClosingBracket = left.indexOfLast { it.isDigit() })
        else compare
    } else if (isAList(left)) {
        val orderedCorrectly = compare(
            left.substring(1, left.findClosingBracketIndex()),
            right.substring(1, right.findClosingBracketIndex())
        )
        if(orderedCorrectly == 0) return orderedCorrectlyAfter(left, right)
        else return orderedCorrectly
    }
    val leftNr = left.substringBefore(',').toInt()
    val rightNr = right.substringBefore(',').toInt()
    if (leftNr < rightNr) return 1
    if (leftNr > rightNr) return -1
    return compare(left.substringAfter(',', ""), right.substringAfter(',', ""))
}

private fun String.addBracketsAroundFirst(): String {
    if(this.all { it.isDigit() }) return "[$this]"
    val afterNr = this.indexOfFirst { !it.isDigit() }
    val nrWithBrackets = "[${this.substring(0, afterNr)}]"
    return if(afterNr >= this.lastIndex) nrWithBrackets
    else "$nrWithBrackets${this.substring(afterNr)}"
}

private fun orderedCorrectlyAfter(
    left: String,
    right: String,
    leftClosingBracket: Int = left.findClosingBracketIndex(),
    rightClosingBracket: Int = right.findClosingBracketIndex()
): Int {
    val leftSubAfter = subStringAfterOrEmpty(left, leftClosingBracket)
    val rightSubAfter = subStringAfterOrEmpty(right, rightClosingBracket)
    return if (leftSubAfter.isEmpty() && rightSubAfter.isNotEmpty()) -1
    else if (leftSubAfter.isEmpty() && rightSubAfter.isEmpty()) 0
    else if(leftSubAfter.isNotEmpty() && rightSubAfter.isEmpty()) 1
    else compare(leftSubAfter.trimStart(','), rightSubAfter.trimStart(','))
}

fun subStringAfterOrEmpty(s: String, closingBracketIndex: Int = s.findClosingBracketIndex()): String {
    return if(closingBracketIndex == s.lastIndex ) ""
    else s.substring(closingBracketIndex + 1)
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