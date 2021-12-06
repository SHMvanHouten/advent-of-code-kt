package com.github.shmvanhouten.adventofcode2021.day06

private const val DAYS_TILL_BIRTH_FOR_RESET_FISH: DaysTillBirth = 6
private const val DAYS_TILL_BIRTH_FOR_NEW_FISH: DaysTillBirth = 8

class SchoolOfFish(private val fishFertilityCounts: Map<DaysTillBirth, NumberOfFish>) {
    fun tick(nrOfTicks: Int): SchoolOfFish {
        return generateSequence(this) { school ->
            school.tick()
        }.drop(nrOfTicks).first()
    }

    fun tick(): SchoolOfFish {
        val newFishFertilityCounts = mutableMapOf<DaysTillBirth, NumberOfFish>()
        for ((daysTillBirth, count) in fishFertilityCounts.entries) {
            if (daysTillBirth > 0) {
                newFishFertilityCounts.merge(daysTillBirth - 1, count, NumberOfFish::plus)
            } else {
                newFishFertilityCounts.merge(DAYS_TILL_BIRTH_FOR_RESET_FISH, count, NumberOfFish::plus)
                newFishFertilityCounts[DAYS_TILL_BIRTH_FOR_NEW_FISH] = count
            }
        }
        return SchoolOfFish(newFishFertilityCounts)
    }

    fun totalFish(): Long {
        return fishFertilityCounts.values.sum()
    }

    constructor(input: String) :
            this(
                input.split(',').map { it.toInt() }.groupingBy { it }.eachCount()
                    .map { (fertility, count) -> fertility to count.toLong() }.toMap()
            )

}

typealias DaysTillBirth = Int
typealias NumberOfFish = Long
