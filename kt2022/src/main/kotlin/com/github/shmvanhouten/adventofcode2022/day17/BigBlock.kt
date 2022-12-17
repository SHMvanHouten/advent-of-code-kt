package com.github.shmvanhouten.adventofcode2022.day17

fun calculateBlockHeightForBigTarget(
    input: String,
    repeatsAt: Int,
    repeatsAtAgain: Int,
    startOfRepeatingBlock: String,
    target: Long = 1_000_000_000_000L
): Long {
    val repetitions = (target - repeatsAt) / (repeatsAtAgain - repeatsAt)
    val remaining = (target - repeatsAt) % (repeatsAtAgain - repeatsAt)

    val draw = draw(Cavern().simulate(input, repeatsAtAgain + remaining.toInt()))
    val repeatingBlock = (draw.substringAfter(startOfRepeatingBlock)
        .substringBefore(startOfRepeatingBlock) + startOfRepeatingBlock).trim()

    val repetitionsHeight = (repetitions - 1) * repeatingBlock.lines().size

    return repetitionsHeight + draw.lines().size
}