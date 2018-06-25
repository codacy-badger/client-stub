package com.github.simy4.stub.core.pattern

import com.github.simy4.stub.core.Request

class RequestPattern(
        private val methodPattern: Pattern<String>,
        private val pathPattern: Pattern<String>,
        private val queryPattern: Pattern<Map<String, String>>,
        private val headersPattern: Pattern<Map<String, Collection<String>>>,
        private val bodyPattern: Pattern<Any>
): Pattern<Request> {
    override fun invoke(request: Request): MatchResult {
        methodPattern(request.method)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}