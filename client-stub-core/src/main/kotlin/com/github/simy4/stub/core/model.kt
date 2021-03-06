package com.github.simy4.stub.core

data class Invocation(
        val request: Request,
        val response: Attempt<Response>
)

data class Request(
        val method: String,
        val path: String,
        val query: Map<String, String>,
        val headers: Map<String, Collection<String>>,
        val body: Any?
)

data class Response(
        val status: Int,
        val headers: Map<String, Collection<String>> = emptyMap(),
        val body: Any? = null
)
