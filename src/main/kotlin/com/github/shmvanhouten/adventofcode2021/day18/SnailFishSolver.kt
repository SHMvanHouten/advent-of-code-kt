package com.github.shmvanhouten.adventofcode2021.day18

fun sum(snailFishNumbers: List<SnailFishNumber>): SnailFishNumber {
    return snailFishNumbers.reduce { acc, snailFishNumber ->
        resolveEntirely(acc + snailFishNumber)
    }
}

fun resolveEntirely(unresolved: SnailFishNumber): SnailFishNumber {
    var snailFishNumber = unresolved
    while (true) {
        val exploded = snailFishNumber.explode()
        if(exploded == snailFishNumber) {
            val split = exploded.split()
            if(split == exploded) return split
            else snailFishNumber = split
        }
        else snailFishNumber = exploded
    }
}
