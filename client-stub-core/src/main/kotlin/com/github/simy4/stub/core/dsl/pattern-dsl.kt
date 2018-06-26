package com.github.simy4.stub.core.dsl

import com.github.simy4.stub.core.pattern.ContainsPattern
import com.github.simy4.stub.core.pattern.EndsWithPattern
import com.github.simy4.stub.core.pattern.EquivPattern
import com.github.simy4.stub.core.pattern.MapEntryPattern
import com.github.simy4.stub.core.pattern.MatchingSafelyPattern
import com.github.simy4.stub.core.pattern.Pattern
import com.github.simy4.stub.core.pattern.RegexPattern
import com.github.simy4.stub.core.pattern.StartsWithPattern
import kotlin.jvm.JvmName

@StubDslMarker
open class PatternDsl<A> {
    private val patterns = mutableListOf<Pattern<A>>()
    internal val pattern: Pattern<A>
        get() = Pattern.any(patterns.toList())

    operator fun A.unaryPlus() {
        matches(EquivPattern(this))
    }

    fun matches(pattern: Pattern<A>) {
        patterns += pattern
    }
}

open class MapPatternDsl<A: Any>: PatternDsl<Map<String, A>>() {
    infix fun String.oneOf(init: PatternDsl<A>.() -> Unit) {
        initPattern(init, PatternDsl()).also { pattern -> matches(MapEntryPattern(this, pattern)) }
    }

    operator fun Pair<String, A>.unaryPlus() {
        matches(MapEntryPattern(first, EquivPattern(second)))
    }
}

open class MultimapPatternDsl<A: Any>: MapPatternDsl<Collection<A>>() {
    infix fun String.hasItems(init: PatternDsl<A>.() -> Unit) {
        initPattern(init, PatternDsl()).also { pattern ->
            matches(MapEntryPattern(this, ContainsPattern(pattern)))
        }
    }

    @JvmName("unarySingularPlus")
    operator fun Pair<String, A>.unaryPlus() {
        matches(MapEntryPattern(first, ContainsPattern(EquivPattern(second))))
    }
}

open class BodyPatternDsl: PatternDsl<Any?>() {
    fun <T> matchesSafely(pattern: Pattern<T>) {
        matches(MatchingSafelyPattern(pattern))
    }
}

internal fun <P: PatternDsl<A>, A> initPattern(init: P.() -> Unit, dsl: P): Pattern<A> =
        dsl.init().let { dsl.pattern }

fun PatternDsl<String>.matches(pattern: String) {
    matches(RegexPattern(pattern))
}

fun PatternDsl<String>.startsWith(prefix: String) {
    matches(StartsWithPattern(prefix))
}

fun PatternDsl<String>.endsWith(postfix: String) {
    matches(EndsWithPattern(postfix))
}

fun <I: Iterable<A>, A> PatternDsl<I>.contains(value: A) {
    matches(ContainsPattern(EquivPattern(value)))
}
