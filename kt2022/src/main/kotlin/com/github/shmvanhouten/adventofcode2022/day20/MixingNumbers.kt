package com.github.shmvanhouten.adventofcode2022.day20

fun mix(input: String): List<Int> {
    return mix(input.lines().map { it.toInt() })
}

fun mix(input: List<Int>): List<Int> {
    val lastIndex = input.lastIndex
    val sequence = input.withIndex()
    val mutableSequence = sequence.toMutableList()
//    println(input)
    sequence.forEach {
        val indexInMutatedSequence = mutableSequence.indexOf(it)
        mutableSequence.removeAt(indexInMutatedSequence)
        var newIndex = (indexInMutatedSequence + it.value) % mutableSequence.size
        if(newIndex < 0) newIndex += mutableSequence.size
//        println("${it.value} from $indexInMutatedSequence to $newIndex")
        mutableSequence.add(newIndex, it)
//        println(mutableSequence.map { it.value })
    }

    return mutableSequence.map { it.value }
}

val List<Int>.groveCoordinates: Int
    get() {
        val indexOfZero = indexOf(0)
        return this[(indexOfZero + 1000) % size] + this[(indexOfZero + 2000) % size] + this[(indexOfZero + 3000) % size]
    }