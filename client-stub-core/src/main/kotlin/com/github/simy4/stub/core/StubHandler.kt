package com.github.simy4.stub.core

import com.github.simy4.stub.core.dsl.MethodStubDsl
import com.github.simy4.stub.core.pattern.ExactMatch
import com.github.simy4.stub.core.pattern.NoMatch
import com.github.simy4.stub.core.pattern.PartialMatch
import com.github.simy4.stub.core.session.StubSession

abstract class StubHandler<RequestT, ResponseT>: StubHandlerSupport<RequestT, ResponseT> {

    fun stub(init: MethodStubDsl.() -> Unit): MethodStub {
        val dsl = MethodStubDsl(this)
        dsl.init()
        return dsl.methodStub
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
                    is PartialMatch -> TODO()
                    is NoMatch -> null
                }} ?: TODO()
    }

}