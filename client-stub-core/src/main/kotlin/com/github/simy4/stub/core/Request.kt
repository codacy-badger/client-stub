package com.github.simy4.stub.core

data class Request(
        val method: String,
        val path: String,
        val query: Map<String, String>,
        val headers: Map<String, Collection<String>>,
        val body: Any?
)
