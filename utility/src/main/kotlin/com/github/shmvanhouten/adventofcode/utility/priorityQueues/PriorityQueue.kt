package com.github.shmvanhouten.adventofcode.utility.priorityQueues

import java.util.*

fun <T> priorityQueueOf(items: List<T>, comparator: Comparator<T>): PriorityQueue<T> =
    PriorityQueue(comparator).apply { addAll(items) }
