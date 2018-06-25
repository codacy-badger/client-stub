package com.github.simy4.stub.core

import com.github.simy4.stub.core.dsl.MethodStubDsl

object Stub {
    fun stubFor(stubHandler: StubHandler<*, *>, init: MethodStubDsl.() -> Unit): MethodStub {
        val dsl = MethodStubDsl(stubHandler)
        dsl.init()
        return dsl.methodStub
    }
}
