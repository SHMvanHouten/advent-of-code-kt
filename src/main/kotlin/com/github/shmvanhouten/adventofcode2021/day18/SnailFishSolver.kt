package com.github.shmvanhouten.adventofcode2021.day18

fun sum(snailFishNumbers: List<SnailFishNumber>): SnailFishNumber {
    return snailFishNumbers.reduce { acc, snailFishNumber ->
        resolveEntirely(acc + snailFishNumber)
    }
}

fun resolveEntirely(unresolved: SnailFishNumber): SnailFishNumber {
    var snailFishNumber = unresolved
    while (true) {
        val resolved = snailFishNumber.resolve()
        if(resolved == snailFishNumber) return snailFishNumber
        else snailFishNumber = resolved
    }
}
