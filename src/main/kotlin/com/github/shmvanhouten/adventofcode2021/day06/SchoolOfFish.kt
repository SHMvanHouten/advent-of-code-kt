package com.github.shmvanhouten.adventofcode2021.day06

class SchoolOfFish(private val fishFertilityCounts: Map<Int, Long>) {
    fun tick(nrOfTicks: Int): SchoolOfFish {
        return 0.until(nrOfTicks).fold(this) {school, _ ->
            school.tick()
        }
    }

    fun tick(): SchoolOfFish {
        val newFishFertilityCounts = mutableMapOf<Int, Long>()
        for ((fertility, count) in fishFertilityCounts.entries) {
            if(fertility > 0) {
                newFishFertilityCounts.merge(fertility - 1, count, Long::plus)
            } else {
                newFishFertilityCounts.merge(6, count, Long::plus)
                newFishFertilityCounts[8] = count
            }
        }
        return SchoolOfFish(newFishFertilityCounts)
    }

    fun totalFish(): Long {
        return fishFertilityCounts.values.sum()
    }

    constructor(input: String) :
        this(input.split(',').map { it.toInt() }.groupingBy { it }.eachCount().map { (fertility, count) -> fertility to count.toLong() }.toMap())




}
