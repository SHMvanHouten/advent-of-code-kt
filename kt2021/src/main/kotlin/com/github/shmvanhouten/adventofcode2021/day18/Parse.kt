package com.github.shmvanhouten.adventofcode2021.day18

fun parseSnailFishNumber(input: String): List<SnailFishNumber> {
    return input.lines().map { toSnailFishNumber(it) }
}

fun toSnailFishNumber(line: String): SnailFishNumber {
    return evaluateFishNumber(line.substring(1, line.lastIndex))
}

fun evaluateFishNumber(line: String): SnailFishNumber {
    if(line.first() != '[' && line.last() != ']') {
        val (n1, n2) = line.split(',')
        return SnumberPair(RegularNumber(n1.toInt()), RegularNumber(n2.toInt()))

    } else if(line.first() != '[') {
        return SnumberPair(
            RegularNumber(line.substring(0, line.indexOf(',')).toInt()),
            toSnailFishNumber(line.substring(line.indexOf(',') + 1))
        )

    } else if(line.last() != ']') {
        return SnumberPair(
            toSnailFishNumber(line.substring(0, line.lastIndexOf(','))),
            RegularNumber(line.substring(line.lastIndexOf(',') + 1).toInt())
        )

    } else {
        val splitIndex = findIndexOfCommaAfterClosingBracketThatMatchesOpeningBracket(line)
        return SnumberPair(
            toSnailFishNumber(line.substring(0, splitIndex)),
            toSnailFishNumber(line.substring(splitIndex + 1))
        )
    }
}

fun findIndexOfCommaAfterClosingBracketThatMatchesOpeningBracket(line: String): Int {
    var nrOfOpeningBrackets = 0
    for (index in line.indices) {
        val c = line[index]
        if (c == '[') nrOfOpeningBrackets++
        else if(c == ']') nrOfOpeningBrackets--
        if(c == ',' && nrOfOpeningBrackets == 0) return index
    }
    error("comma not found in $line")
}
