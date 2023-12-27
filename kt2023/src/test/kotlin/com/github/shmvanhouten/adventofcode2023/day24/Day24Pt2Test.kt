package com.github.shmvanhouten.adventofcode2023.day24

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day24Pt2Test {

    // """24, 13, 10 @ -3, 1, 2"""
    private val example = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    /*
       if we reduce the velocity by the same amount for every dimension we can
       treat the problem as if our rock is standing still

       then we could say some things:
       given 2 rocks of the same dimension with the same velocity:
       for each prime, there needs to be a prime where it % prime is the same for both
       otherwise the two rocks would never intersect,
       which means that for our example, where we have
       {x = 19, vx = -2}, {x = 20, vx = -2} we know that vx must be reduced to either
       1 or -1, because otherwise the two numbers could never end up in the same place.
    */
    @Test
    fun `given these dimensions, they must move at these speeds to work`() {
        expectThat(input.lines().map { toRock(it) }
            .map { it.loc.y to it.velocity.y }
            .findVelocityDifferenceNecessaryToConverge()).isEqualTo(305.toBigInteger())
        expectThat(input.lines().map { toRock(it) }
            .map { it.loc.x to it.velocity.x }
            .findVelocityDifferenceNecessaryToConverge()).isEqualTo(44.toBigInteger())
        expectThat(input.lines().map { toRock(it) }
            .map { it.loc.z to it.velocity.z }
            .findVelocityDifferenceNecessaryToConverge()).isEqualTo(75.toBigInteger())
    }

    @Test
    @Disabled("doesn't work for example")
    fun `example 2`() {
        // Doesn't work, because we don't have enough to differentiate on the z axis
        // we could make it work, but that would make our
        expectThat(findRockThatHitsAllHailstones(example))
            .isEqualTo(toRock("""24, 13, 10 @ -3, 1, 2"""))
    }

    @Test
    fun `that means that the velocity for our rock is 44, 305, 75`() {
        val result = findVelocityForRock(input)
        expectThat(result).isEqualTo(Big3dCoordinate(44.toBigInteger(), 305.toBigInteger(), 75.toBigInteger()))
    }

    @Test
    fun `given we know the velocities of our stone, the location must be where all others intersect`() {
        val velocity = Big3dCoordinate((-3).toBigInteger(), 1.toBigInteger(), 2.toBigInteger())
        val result = example.lines().map { toRock(it) }
            .intersectAtGivenVelocityDifference(velocity)
        expectThat(result)
            .isEqualTo(Big3dCoordinate(24.toBigInteger(), 13.toBigInteger(), 10.toBigInteger()))
        expectThat(result.x + result.y + result.z)
            .isEqualTo(47.toBigInteger())
    }

    @Test
    fun `which means that our rock is at 234382970331570, 100887864960615, 231102671115832`() {
        val result = findRockThatHitsAllHailstones(input)
        expectThat(result.loc)
            .isEqualTo(
                Big3dCoordinate(234382970331570.toBigInteger(), 100887864960615.toBigInteger(), 231102671115832.toBigInteger())
            )
    }

    @Test
    fun `part 2`() {
        val result = findRockThatHitsAllHailstones(input).loc
        expectThat(result.x + result.y + result.z)
            .isEqualTo(566373506408017.toBigInteger())
    }


    private val input by lazy { readFile("/input-day24.txt") }
}