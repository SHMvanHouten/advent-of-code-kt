package com.github.shmvanhouten.adventofcode.utility.coordinate

enum class Degree(val degree: Int) {
    D90(90),
    D180(180),
    D270(270);

    fun amountOf90DegreeTurns(): Int {
        return degree / 90
    }
}