package com.github.simy4.stub.core

import com.github.simy4.stub.core.pattern.Pattern
import com.github.simy4.stub.core.session.StubSession

class MethodStub internal constructor(
        private val stubHandler: StubHandler<*, *>,
        internal val requestPattern: Pattern<Request>,
        internal val responseSupplier: (Request) -> Attempt<Response>
) {
    fun wasInvoked(): Boolean = !invocations().isEmpty()

    fun invocations(): List<Invocation> = StubSession.sessionFor(stubHandler).invocations(this)
}
