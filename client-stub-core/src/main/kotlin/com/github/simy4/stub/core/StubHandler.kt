package com.github.simy4.stub.core

import com.github.simy4.stub.core.dsl.MethodStubDsl
import com.github.simy4.stub.core.dsl.ResponseSupplierDsl
import com.github.simy4.stub.core.pattern.ExactMatch
import com.github.simy4.stub.core.session.StubSession

abstract class StubHandler<RequestT, ResponseT>: StubHandlerSupport<RequestT, ResponseT> {

    fun stub(init: MethodStubDsl.() -> Unit): MethodStub {
        val dsl = MethodStubDsl(this)
        dsl.init()
        return dsl.methodStub
    }

    fun stubDefault(init: ResponseSupplierDsl.() -> Unit) {
        val dsl = ResponseSupplierDsl()
        dsl.init()
        StubSession.sessionFor(this).defaultStub = dsl.supplier
    }

    operator fun invoke(requestT: RequestT): Attempt<ResponseT> =
            invoke0(requestT.inject).flatMap { it.project(requestT) }

    private fun invoke0(request: Request): Attempt<Response> {
        val session = StubSession.sessionFor(this)
        return session.stubs
                .map { stub -> stub.requestPattern(request) to stub }
                .minBy { it.first }
                ?.let { pair -> when(pair.first) {
                    is ExactMatch -> {
                        val response = pair.second.responseSupplier(request)
                        session.recordInvocation(pair.second, Invocation(request, response))
                        response
                    }
                    else -> null
                }} ?: session.defaultStub(request)
    }

}