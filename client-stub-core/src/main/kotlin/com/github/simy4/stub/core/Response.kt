package com.github.simy4.stub.core

data class Response(
        val status: Int,
        val headers: Map<String, Collection<String>> = emptyMap(),
        val body: Any? = null
)
