package com.github.simy4.stub.core.dsl

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.MethodStub
import com.github.simy4.stub.core.StubHandler
import com.github.simy4.stub.core.Success
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response
import com.github.simy4.stub.core.pattern.Pattern
import com.github.simy4.stub.core.session.StubSession

@DslMarker
annotation class StubDslMarker

@StubDslMarker
class MethodStubDsl internal constructor(private val stubHandler: StubHandler<*, *>) {
    private var request: Pattern<Request> = Pattern.none()
    private var response: (Request) -> Attempt<Response> = { Success(Response(404, emptyMap(), null)) }
    internal val methodStub: MethodStub
        get() {
            val methodStub = MethodStub(stubHandler, request, response)
            StubSession.sessionFor(stubHandler).stub(methodStub)
            return methodStub
        }

    fun request(init: RequestPatternDsl.() -> Unit) {
        val requestStub = RequestPatternDsl()
        requestStub.init()
        request = requestStub.request
    }

    fun response(init: ResponseSupplierDsl.() -> Unit) {
        val responseDsl = ResponseSupplierDsl()
        responseDsl.init()
        response = responseDsl.supplier
    }

    fun supplier(supplier: (Request) -> Attempt<Response>) {
        response = supplier
    }
}
