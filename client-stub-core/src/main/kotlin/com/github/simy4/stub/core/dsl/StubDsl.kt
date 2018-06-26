package com.github.simy4.stub.core.dsl

@DslMarker
annotation class StubDslMarker

@StubDslMarker
interface StubDsl<R> {
    val result: R
}

internal inline fun <D: StubDsl<A>, A> initDsl(init: D.() -> Unit, dsl: D): A = dsl.init().let { dsl.result }
