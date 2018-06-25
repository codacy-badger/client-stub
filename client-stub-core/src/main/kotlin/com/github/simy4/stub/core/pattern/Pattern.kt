package com.github.simy4.stub.core.pattern

interface Pattern<in A>: (A) -> MatchResult {
    companion object {
        fun <A> all(): Pattern<A> = object : Pattern<A> {
            override fun invoke(a: A): MatchResult = ExactMatch
        }

        fun <A> any(patterns: Iterable<Pattern<A>>): Pattern<A> = object : Pattern<A> {
            override fun invoke(a: A): MatchResult = patterns.map { p -> p(a) }.fold(NoMatch, MatchResult::or)
        }

        fun <A> none(): Pattern<A> = object : Pattern<A> {
            override fun invoke(a: A): MatchResult = NoMatch
        }
    }
}
