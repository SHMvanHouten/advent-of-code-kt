package com.github.shmvanhouten.adventofcode2021.day18

sealed class SnailFishNumber {
    operator fun plus(other: SnailFishNumber): SnumberPair {
        return SnumberPair(this, other)
    }

    abstract fun split(): SnailFishNumber
    abstract fun magnitude(): Long
    fun explode(): SnailFishNumber {
        return explode(0).result
    }

    abstract fun explode(depth: Int): ExplosionResult

    abstract fun addToLeftMost(value: Int): SnailFishNumber
    abstract fun addToRightMost(value: Int): SnailFishNumber
}

data class RegularNumber(val value: Int) : SnailFishNumber() {
    override fun magnitude(): Long = value.toLong()

    override fun split(): SnailFishNumber =
        if (value >= 10) SnumberPair(RegularNumber(value.floorDiv(2)), RegularNumber(value.ceilDiv(2)))
        else this

    override fun explode(depth: Int): ExplosionResult =
        ExplosionResult(this)

    override fun addToLeftMost(value: Int): SnailFishNumber =
        RegularNumber(this.value + value)

    override fun addToRightMost(value: Int): SnailFishNumber =
        RegularNumber(this.value + value)

    override fun toString(): String = value.toString()
}

data class SnumberPair(val left: SnailFishNumber, val right: SnailFishNumber) : SnailFishNumber() {
    override fun magnitude(): Long {
        return 3 * left.magnitude() + 2 * right.magnitude()
    }

    override fun split(): SnailFishNumber {
        return attemptToSplitLeft()
            ?: attemptToSplitRight()
            ?: this
    }

    override fun explode(depth: Int): ExplosionResult {
        return if (depth >= 3) {
            explodeOneOfTheChildNodesAtDepth4()

        } else {
            attemptToExplodeLeft(depth)
                ?: attemptToExplodeRight(depth)
                ?: ExplosionResult(this)
        }
    }

    private fun attemptToSplitLeft(): SnailFishNumber? {
        val split = left.split()
        return if (split != left) return this.copy(left = split)
        else null
    }

    private fun attemptToSplitRight(): SnailFishNumber? {
        val split = right.split()
        return if (split != right) this.copy(right = split)
        else null
    }

    private fun explodeOneOfTheChildNodesAtDepth4(): ExplosionResult = when {
        left is RegularNumber && right is RegularNumber -> {
            ExplosionResult(this)
        }
        left is SnumberPair -> {
            val (val1, val2) = left.explodeValues()
            ExplosionResult(
                SnumberPair(RegularNumber(0), right.addToLeftMost(val2)),
                val1
            )
        }
        right is SnumberPair -> {
            val (val1, val2) = right.explodeValues()
            ExplosionResult(
                SnumberPair(left.addToRightMost(val1), RegularNumber(0)),
                rightOverflow = val2
            )
        }
        else -> error("either left or right is an unknown type, $this")
    }

    private fun attemptToExplodeLeft(depth: Int): ExplosionResult? {
        val (resultingLeftSnailFish, leftOverflow, rightOverflow) = left.explode(depth + 1)
        return when {
            leftOverflow != null -> {
                ExplosionResult(
                    this.copy(left = resultingLeftSnailFish),
                    leftOverflow
                )
            }
            rightOverflow != null -> {
                ExplosionResult(
                    SnumberPair(left = resultingLeftSnailFish, right = right.addToLeftMost(rightOverflow)),
                    rightOverflow = 0
                )
            }
            else -> {
                null
            }
        }
    }

    private fun attemptToExplodeRight(depth: Int): ExplosionResult? {
        val explode = right.explode(depth + 1)
        val (resultingRightSnailFish, leftOverflow, rightOverflow) = explode
        return when {
            leftOverflow != null -> {
                ExplosionResult(
                    SnumberPair(
                        left = left.addToRightMost(leftOverflow),
                        right = resultingRightSnailFish
                    ),
                    leftOverflow = 0
                )
            }
            rightOverflow != null -> {
                ExplosionResult(
                    this.copy(right = resultingRightSnailFish),
                    rightOverflow = rightOverflow
                )
            }
            else -> {
                null
            }
        }
    }

    private fun explodeValues(): Pair<Int, Int> {
        if (this.left !is RegularNumber || this.right !is RegularNumber) error("$this contains a SnumberPair at depth 5!")
        return left.value to right.value
    }

    override fun addToLeftMost(value: Int): SnailFishNumber {
        return this.copy(left = left.addToLeftMost(value))
    }

    override fun addToRightMost(value: Int): SnailFishNumber {
        return this.copy(right = right.addToRightMost(value))
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}

data class ExplosionResult(
    val result: SnailFishNumber,
    val leftOverflow: Int? = null,
    val rightOverflow: Int? = null
)

private fun Int.ceilDiv(divisor: Int): Int {
    return if (this % divisor != 0) this.div(divisor) + 1
    else this.div(divisor)
}
