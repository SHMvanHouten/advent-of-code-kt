package com.github.shmvanhouten.adventofcode2022.day20

private const val DECRYPTION_KEY = 811589153L

fun mix(input: String): List<Long> {
    return mix(input.lines().map { it.toLong() }.withIndex()).map { it.value }
}

fun megaMix(input: String): List<Long> {
    var list = input.lines().map { it.toLong() * DECRYPTION_KEY }.withIndex()
    repeat(10) {
        list = mix(list)
    }
    return list.map { it.value }
}

fun mix(numbers: Iterable<IndexedValue<Long>>): MutableList<IndexedValue<Long>> {
    val mutableSequence = numbers.toMutableList()
    numbers.sortedBy { it.index }.forEach {
        val indexInMutatedSequence = mutableSequence.indexOf(it)
        mutableSequence.removeAt(indexInMutatedSequence)
        var newIndex = ((indexInMutatedSequence + it.value) % mutableSequence.size).toInt()
        if(newIndex < 0) newIndex += mutableSequence.size
        mutableSequence.add(newIndex, it)
    }

    return mutableSequence
}

val List<Long>.groveCoordinates: Long
    get() {
        val indexOfZero = indexOf(0)
        return this[(indexOfZero + 1000) % size] + this[(indexOfZero + 2000) % size] + this[(indexOfZero + 3000) % size]
    }
