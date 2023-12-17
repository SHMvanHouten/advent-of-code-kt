package com.github.shmvanhouten.adventofcode.utility.priorityQueues

import java.util.*

fun <T> priorityQueueOf(item: T, comparator: Comparator<T>): PriorityQueue<T> =
    PriorityQueue(comparator).apply { add(item) }