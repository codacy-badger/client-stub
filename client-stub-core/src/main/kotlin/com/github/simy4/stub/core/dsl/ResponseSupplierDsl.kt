package com.github.simy4.stub.core.dsl

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Success
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response

@StubDslMarker
class ResponseSupplierDsl {
    private var _status: Int = 404
    private var _headers: Map<String, Collection<String>> = mutableMapOf()
    private var _body: Any? = null
    internal val supplier: (Request) -> Attempt<Response>
        get() {
            val status = _status
            val headers = _headers
            val body = _body
            return { Success(Response(status, headers, body)) }
        }

    fun status(init: SupplierDsl<Int>.() -> Unit) {
        _status = initSupplier(init, SupplierDsl(404))
    }

    fun headers(init: SupplierDsl<Map<String, Collection<String>>>.() -> Unit) {
        _headers = initSupplier(init, SupplierDsl<Map<String, Collection<String>>>(mutableMapOf()))
    }

    fun body(init: SupplierDsl<Any>.() -> Unit) {
        _body = initSupplier(init, SupplierDsl<Any>(""))
    }
}