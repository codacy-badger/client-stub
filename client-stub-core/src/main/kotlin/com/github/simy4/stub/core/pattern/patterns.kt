package com.github.simy4.stub.core.pattern

class EquivPattern<in A>(private val match: A): Pattern<A> {
    override fun invoke(value: A): MatchResult = MatchResult.match(match == value)
}

class MatchingSafelyPattern<in A>(private val pattern: Pattern<A>): Pattern<Any> {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(value: Any): MatchResult =
            try { pattern(value as A) }
            catch (cce: ClassCastException) { NoMatch }
}

class MapEntryPattern<K, in A>(private val key: K, private val pattern: Pattern<A>): Pattern<Map<K, A>> {
    override fun invoke(map: Map<K, A>): MatchResult = map[key]?.let(pattern) ?: NoMatch
}

class ContainsPattern<in A>(private val pattern: Pattern<A>): Pattern<Iterable<A>> {
    override fun invoke(iterable: Iterable<A>): MatchResult = iterable.map(pattern).fold(NoMatch, MatchResult::or)
}
