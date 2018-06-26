package com.github.simy4.stub.core

import com.github.simy4.stub.core.pattern.Pattern
import com.github.simy4.stub.core.session.StubSession

class MethodStub internal constructor(
        private val stubHandler: StubHandler<*, *>,
        internal val requestPattern: Pattern<Request>,
        internal val responseSupplier: (Request) -> Attempt<Response>
) {
    val invoked: Boolean
        get() = !invocations.isEmpty()

    val invocations: List<Invocation>
        get() = StubSession.sessionFor(stubHandler).invocations(this)
}
