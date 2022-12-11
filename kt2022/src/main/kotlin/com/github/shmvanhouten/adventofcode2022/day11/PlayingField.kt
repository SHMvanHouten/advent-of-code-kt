package com.github.shmvanhouten.adventofcode2022.day11

class PlayingField(val monkeys: List<Monkey>) {
    fun playRounds(nrOfRounds: Int) {
        val allMonkeyValues = (2* 13* 5* 3* 11* 17* 7* 19).toBigInteger()
        repeat(nrOfRounds) {
            for (monkey in monkeys) {
                monkey.doMonkeyBusiness(monkeys, allMonkeyValues)
            }
        }
    }

    fun monkeyBusiness(): Long {
        return monkeys
            .map { it.timesThrown }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }

}
