package com.github.simy4.stub.core.dsl

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Success
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response

class ResponseSupplierDsl: StubDsl<(Request) -> Attempt<Response>> {
    private var _status: Int = 404
    private var _headers: Map<String, Collection<String>> = mutableMapOf()
    private var _body: Any? = null
    override val result: (Request) -> Attempt<Response>
        get() {
            val status = _status
            val headers = _headers
            val body = _body
            return { Success(Response(status, headers, body)) }
        }

    fun status(init: SupplierDsl<Int>.() -> Unit) {
        _status = initDsl(init, SupplierDsl<Int>(404))
    }

    fun headers(init: MultimapSupplierDsl<String>.() -> Unit) {
        _headers = initDsl(init, MultimapSupplierDsl<String>())
    }

    fun body(init: BodySupplierDsl.() -> Unit) {
        _body = initDsl(init, BodySupplierDsl())
    }
}