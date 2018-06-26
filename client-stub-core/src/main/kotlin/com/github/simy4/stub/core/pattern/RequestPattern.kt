package com.github.simy4.stub.core.pattern

import com.github.simy4.stub.core.Request

class RequestPattern(
        private val methodPattern: Pattern<String>,
        private val pathPattern: Pattern<String>,
        private val queryPattern: Pattern<Map<String, String>>,
        private val headersPattern: Pattern<Map<String, Collection<String>>>,
        private val bodyPattern: Pattern<Any?>
): Pattern<Request> {
    override fun invoke(request: Request): MatchResult =
            Weighted(pathPattern(request.path), 10.0) and Weighted(methodPattern(request.method), 5.0) and
                    queryPattern(request.query) and headersPattern(request.headers) and bodyPattern(request.body)
}