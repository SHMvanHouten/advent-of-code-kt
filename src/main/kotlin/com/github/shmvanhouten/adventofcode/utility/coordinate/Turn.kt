package com.github.shmvanhouten.adventofcode.utility.coordinate

enum class Turn {
    FORWARD,
    RIGHT,
    BACK,
    LEFT;


    fun by(by: Degree): Turn {
        return when(this) {
            FORWARD -> error("can't move forward by an amount of degrees")
            RIGHT -> values()[(this.ordinal + (by.amountOf90DegreeTurns() - 1)) % values().size]
            BACK -> error("can't move backwards by an amount of degrees")
            LEFT -> values()[(this.ordinal - (by.amountOf90DegreeTurns() - 1)) % values().size]
        }
    }
}