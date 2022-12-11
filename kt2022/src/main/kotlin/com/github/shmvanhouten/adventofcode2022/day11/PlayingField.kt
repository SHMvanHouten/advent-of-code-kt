package com.github.shmvanhouten.adventofcode2022.day11

class PlayingField(val monkeys: List<Monkey>) {

    fun playSimpleRounds(
        nrOfRounds: Int = 20,
        unWorryOperation: (WorryLevel) -> WorryLevel = { it / 3 }
    ) {
        repeat(nrOfRounds) {
            for (monkey in monkeys) {
                monkey.doMonkeyBusiness(monkeys, unWorryOperation)
            }
        }
    }

    fun playRounds(nrOfRounds: Int = 10000) {
        val monkeyTestValues = monkeys
            .map { it.testValue }
            .reduce(Long::times)
        playSimpleRounds(nrOfRounds) { it % monkeyTestValues }
    }

    fun monkeyBusiness(): Long {
        return monkeys
            .map { it.timesThrown }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }
}
