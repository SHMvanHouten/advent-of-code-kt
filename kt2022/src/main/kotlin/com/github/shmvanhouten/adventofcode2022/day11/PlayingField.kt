package com.github.shmvanhouten.adventofcode2022.day11

class PlayingField(val monkeys: List<Monkey>) {
    fun playRounds(nrOfRounds: Int) {
        repeat(nrOfRounds) {
            for (monkey in monkeys) {
                monkey.doMonkeyBusiness(monkeys)
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
