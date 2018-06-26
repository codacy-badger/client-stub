package com.github.simy4.stub.core.dsl

import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.pattern.Pattern
import com.github.simy4.stub.core.pattern.RequestPattern

@StubDslMarker
class RequestPatternDsl {
    private var methodPattern: Pattern<String> = Pattern.all()
    private var pathPattern: Pattern<String> = Pattern.all()
    private var queryPattern: Pattern<Map<String, String>> = Pattern.all()
    private var headersPattern: Pattern<Map<String, Collection<String>>> = Pattern.all()
    private var bodyPattern: Pattern<Any?> = Pattern.all()
    internal val request: Pattern<Request>
        get() = RequestPattern(methodPattern, pathPattern, queryPattern, headersPattern, bodyPattern)

    fun method(init: PatternDsl<String>.() -> Unit) {
        methodPattern = initPattern(init, PatternDsl())
    }

    fun path(init: PatternDsl<String>.() -> Unit) {
        pathPattern = initPattern(init, PatternDsl())
    }

    fun query(init: MapPatternDsl<String>.() -> Unit) {
        queryPattern = initPattern(init, MapPatternDsl<String>())
    }

    fun headers(init: MultimapPatternDsl<String>.() -> Unit) {
        headersPattern = initPattern(init, MultimapPatternDsl<String>())
    }

    fun body(init: PatternDsl<Any?>.() -> Unit) {
        bodyPattern = initPattern(init, BodyPatternDsl())
    }
}
