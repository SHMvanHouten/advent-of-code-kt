package com.github.shmvanhouten.adventofcode.utility.coordinate.coordinate3d.mapping

import com.github.shmvanhouten.adventofcode.utility.coordinate.Coordinate3d
import com.github.shmvanhouten.adventofcode.utility.coordinate.negate

val mappingInAllDirections: List<(Coordinate3d) -> Coordinate3d> =
    listOf(
        { (a, b, c) -> Coordinate3d(a, b, c) },
        { (a, b, c) -> Coordinate3d(a, b.negate(), c.negate()) },
        { (a, b, c) -> Coordinate3d(a, c, b.negate()) },
        { (a, b, c) -> Coordinate3d(a, c.negate(), b) },

        { (a, b, c) -> Coordinate3d(a.negate(), b, c.negate()) },
        { (a, b, c) -> Coordinate3d(a.negate(), b.negate(), c) },
        { (a, b, c) -> Coordinate3d(a.negate(), c, b) },
        { (a, b, c) -> Coordinate3d(a.negate(), c.negate(), b.negate()) },

        { (a, b, c) -> Coordinate3d(b, a.negate(), c) },
        { (a, b, c) -> Coordinate3d(b, a, c.negate()) },
        { (a, b, c) -> Coordinate3d(b, c, a) },
        { (a, b, c) -> Coordinate3d(b, c.negate(), a.negate()) },

        { (a, b, c) -> Coordinate3d(b.negate(), a, c) },
        { (a, b, c) -> Coordinate3d(b.negate(), a.negate(), c.negate()) },
        { (a, b, c) -> Coordinate3d(b.negate(), c, a.negate()) },
        { (a, b, c) -> Coordinate3d(b.negate(), c.negate(), a) },

        { (a, b, c) -> Coordinate3d(c, b, a.negate()) },
        { (a, b, c) -> Coordinate3d(c, b.negate(), a) },
        { (a, b, c) -> Coordinate3d(c, a, b) },
        { (a, b, c) -> Coordinate3d(c, a.negate(), b.negate()) },

        { (a, b, c) -> Coordinate3d(c.negate(), b, a) },
        { (a, b, c) -> Coordinate3d(c.negate(), b.negate(), a.negate()) },
        { (a, b, c) -> Coordinate3d(c.negate(), a, b.negate()) },
        { (a, b, c) -> Coordinate3d(c.negate(), a.negate(), b) },
    )