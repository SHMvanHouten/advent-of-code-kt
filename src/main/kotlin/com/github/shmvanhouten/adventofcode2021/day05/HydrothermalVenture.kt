package com.github.shmvanhouten.adventofcode2021.day05

import com.github.shmvanhouten.adventofcode2020.coordinate.Coordinate

fun countOverlappingLocations(coordinates: Map<Coordinate, Occurrence>) =
    coordinates.count { (_, occurrence) -> occurrence > 1 }