package com.github.shmvanhouten.adventofcode2021.coordinate3d

import com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.Coordinate3dComparator
import com.github.shmvanhouten.adventofcode2021.day19.parse
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Test

class Coordinate3dTest {

    @Test
    internal fun `scanners are all the same rotated in a different way`() {
        val unRotatedScanner = scanners[0]
        val map = scanners.map { it.coordinates.sortedWith(Coordinate3dComparator()) }
        val rotations = unRotatedScanner
            .facingEveryWhichWay()
            .map { it.first }
            .map { it.sortedWith(Coordinate3dComparator()) }
            .toSet()

        assertThat(
            rotations,
            hasSize(equalTo(24))
        )

        map.forEach { beacons ->
            assertThat(rotations.contains(beacons), equalTo(true))
        }
    }
}

val scanners = """--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7

--- scanner 0 ---
1,-1,1
2,-2,2
3,-3,3
2,-1,3
-5,4,-6
-8,-7,0

--- scanner 0 ---
-1,-1,-1
-2,-2,-2
-3,-3,-3
-1,-3,-2
4,6,5
-7,0,8

--- scanner 0 ---
1,1,-1
2,2,-2
3,3,-3
1,3,-2
-4,-6,5
7,0,8

--- scanner 0 ---
1,1,1
2,2,2
3,3,3
3,1,2
-6,-4,-5
0,7,-8""".let { parse(it) }