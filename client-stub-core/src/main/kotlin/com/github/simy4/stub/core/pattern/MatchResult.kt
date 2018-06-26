package com.github.simy4.stub.core.pattern

import kotlin.math.abs

sealed class MatchResult: Comparable<MatchResult> {
    companion object {
        fun match(test: Boolean): MatchResult = if (test) ExactMatch else NoMatch

        fun match(distance: Double): MatchResult = when(distance) {
            0.0 -> ExactMatch
            Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY -> NoMatch
            else -> PartialMatch(distance)
        }
    }

    abstract val distance: Double

    infix fun and(other: MatchResult): MatchResult = if (this <= other) other else this

    infix fun or(other: MatchResult): MatchResult = if (this <= other) this else other

    override fun compareTo(other: MatchResult): Int = abs(distance).compareTo(abs(other.distance))
}

object ExactMatch: MatchResult() {
    override val distance: Double = 0.0
}

data class PartialMatch(override val distance: Double): MatchResult()

object NoMatch: MatchResult() {
    override val distance: Double = Double.POSITIVE_INFINITY
}

internal data class Weighted(val result: MatchResult, val weight: Double = 1.0): MatchResult() {
    override val distance: Double = result.distance * weight
}
