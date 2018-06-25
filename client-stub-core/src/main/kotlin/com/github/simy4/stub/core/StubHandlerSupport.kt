package com.github.simy4.stub.core

interface StubHandlerSupport<RequestT, ResponseT> {
    val RequestT.inject: Request

    fun Response.project(requestT: RequestT): Attempt<ResponseT>
}