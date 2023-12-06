package com.github.shmvanhouten.adventofcode2023.day06

fun toRaceHighscores(input: String): List<Highscore> {
    val (times, distances) = input.lines().map {
        it.substringAfter(":").trim().split(" ").filter { it.isNotBlank() }.map(String::toInt)
    }
    return times.zip(distances).map { Highscore(it.first, it.second.toLong()) }
}

data class Highscore (
    val time: Int,
    val bestDistance: Long
) {
    fun waysToBeat(): Int {

        return ((time/2)).downTo(0)
            .asSequence()
            .map { race(it, time) }
            .takeWhile { reachesFartherThan(it, bestDistance) }.count() +
        (time/2).until(Int.MAX_VALUE)
            .asSequence()
            .map { race(it, time) }
            .takeWhile { reachesFartherThan(it, bestDistance) }
            .count() - 1
    }

    private fun race(timePressed: Int, time: Int) = (time - timePressed) * timePressed

    private fun reachesFartherThan(other: Int, distance: Long): Boolean {
         return other > distance
    }

}


