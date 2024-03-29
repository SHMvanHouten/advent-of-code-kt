package com.github.shmvanhouten.adventofcode2022.day15

import com.github.shmvanhouten.adventofcode.utility.FileReader.readFile
import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate
import com.github.shmvanhouten.adventofcode.utility.coordinate.draw
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day15Test {

    @Nested
    inner class Part1 {

        @Test
        internal fun `given sensor at 1,1 and beacon at 0,0 then missing beacon cannot be between 0,0 and 2,2`() {
            val input = "Sensor at x=1, y=1: closest beacon is at x=0, y=0"
            val impossibleSpots: Set<Coordinate> = findWhereSensorCannotBe(input)
            assertThat(draw(impossibleSpots, '#', '.')).isEqualTo(
                """
                   ..#..
                   .B##.
                   ##S##
                   .###.
                   ..#..
                """.trimIndent().replace('B', '#').replace('S', '#')
            )
            assertThat(impossibleSpots).contains(Coordinate(-1,1))
            assertThat(impossibleSpots).contains(Coordinate(1,-1))
        }

        @Test
        internal fun `given sensor at 10,10 and beacon at 9,9 then missing beacon cannot be between 9,9 and 11,11`() {
            val input = "Sensor at x=10, y=10: closest beacon is at x=9, y=9"
            val impossibleSpots: Set<Coordinate> = findWhereSensorCannotBe(input)
            assertThat(draw(impossibleSpots, '#', '.')).isEqualTo(
                """
                   ..#..
                   .B##.
                   ##S##
                   .###.
                   ..#..
                """.trimIndent().replace('B', '#').replace('S', '#')
            )
            assertThat(impossibleSpots).contains(Coordinate(8,10))
            assertThat(impossibleSpots).contains(Coordinate(12,10))
            assertThat(impossibleSpots).contains(Coordinate(10,8))
            assertThat(impossibleSpots).contains(Coordinate(10,12))
        }

        @Test
        internal fun `given sensor at -1,-1 and beacon at 0,0 then missing beacon cannot be between 0,0 and -2,-2`() {
            val input = "Sensor at x=-1, y=-1: closest beacon is at x=0, y=0"
            val impossibleSpots: Set<Coordinate> = findWhereSensorCannotBe(input)
            assertThat(draw(impossibleSpots, '#', '.')).isEqualTo(
                """
                   ..#..
                   .B##.
                   ##S##
                   .###.
                   ..#..
                """.trimIndent().replace('B', '#').replace('S', '#')
            )
        }

        @Test
        internal fun example() {
            val targetY = 10
            val impossibleSpots = findWhereSensorCannotBeAtY(exampleInput, targetY)
            val beaconCoordinates = findBeaconCoordinates(exampleInput).filter { it.y == targetY }.map { it.x }
            assertThat((impossibleSpots - beaconCoordinates)).hasSize(26)

        }

        @Test
        internal fun `part 1`() {
            val targetY = 2_000_000
            val impossibleSpots = findWhereSensorCannotBeAtY(input, targetY)
            val beaconCoordinates = findBeaconCoordinates(input).filter { it.y == targetY }.map { it.x }
            assertThat(impossibleSpots - beaconCoordinates).hasSize(5832528)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun example() {
            val sensor = findWhereSensorIsInRange(exampleInput, 0, 20)
            assertThat(sensor).isEqualTo(Coordinate(14, 11))
            assertThat(sensor.frequency()).isEqualTo(56000011L)
        }

        @Test
        internal fun `part 2`() {
            val sensor = findWhereSensorIsInRange(input)
            assertThat(sensor.frequency()).isEqualTo(13360899249595L)
        }
    }

    private val input by lazy { readFile("/input-day15.txt")}
    private val exampleInput = """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3
    """.trimIndent()

}
