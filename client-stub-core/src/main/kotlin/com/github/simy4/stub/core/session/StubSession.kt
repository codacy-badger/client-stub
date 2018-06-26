package com.github.simy4.stub.core.session

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Invocation
import com.github.simy4.stub.core.MethodStub
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response
import com.github.simy4.stub.core.StubHandler
import com.github.simy4.stub.core.Success

interface StubSession {

    val stubs: List<MethodStub>

    var defaultStub: (Request) -> Attempt<Response>

    fun stub(methodStub: MethodStub)

    fun invocations(methodStub: MethodStub): List<Invocation>

    fun recordInvocation(methodStub: MethodStub, invocation: Invocation)

    companion object {
        private val globalSession = InternalSession(null)
        private var currentSession = globalSession

        fun startSession() {
            synchronized(this) {
                currentSession = InternalSession(globalSession)
            }
        }

        fun sessionFor(stubHandler: StubHandler<*, *>): StubSession = synchronized(this) {
            currentSession.sessionFor(stubHandler)
        }

        fun endSession() {
            synchronized(this) {
                currentSession = currentSession.parent ?: globalSession
            }
        }
    }

    private class InternalSession(val parent: InternalSession?) {
        private val registry = mutableMapOf<StubHandler<*, *>, Registry>()

        internal fun sessionFor(stubHandler: StubHandler<*, *>): StubSession {
            val registry = registry.getOrPut(stubHandler) { Registry() }
            return object : StubSession {
                override val stubs: List<MethodStub>
                    get() {
                        synchronized(registry) {
                            val currentSessionStubs = registry.methodStubs.toList()
                            val parentSessionStubs = parent?.sessionFor(stubHandler)?.stubs ?: emptyList()
                            return currentSessionStubs + parentSessionStubs
                        }
                    }

                override var defaultStub: (Request) -> Attempt<Response> = {
                    Success(Response(404, emptyMap(), null))
                }

                override fun stub(methodStub: MethodStub) {
                    synchronized(registry) {
                        registry.methodStubs += methodStub
                    }
                }

                override fun invocations(methodStub: MethodStub): List<Invocation> {
                    synchronized(registry) {
                        val currentInvocations = registry.invocations[methodStub]?.toList() ?: emptyList()
                        val parentInvocations = parent?.sessionFor(stubHandler)?.invocations(methodStub) ?: emptyList()
                        return currentInvocations + parentInvocations
                    }
                }

                override fun recordInvocation(methodStub: MethodStub, invocation: Invocation) {
                    synchronized(registry) {
                        registry.invocations.getOrPut(methodStub) { mutableListOf() } += invocation
                    }
                }
            }
        }
    }

    private data class Registry(
            val methodStubs: MutableList<MethodStub> = mutableListOf(),
            val invocations: MutableMap<MethodStub, MutableList<Invocation>> = mutableMapOf()
    )
}
