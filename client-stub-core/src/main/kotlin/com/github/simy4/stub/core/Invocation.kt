package com.github.simy4.stub.core

data class Invocation(
        val request: Request,
        val response: Attempt<Response>
)