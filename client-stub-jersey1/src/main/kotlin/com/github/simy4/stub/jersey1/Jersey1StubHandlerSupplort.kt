package com.github.simy4.stub.jersey1

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response
import com.github.simy4.stub.core.StubHandlerSupport
import com.sun.jersey.api.client.ClientRequest
import com.sun.jersey.api.client.ClientResponse

interface Jersey1StubHandlerSupplort: StubHandlerSupport<ClientRequest, ClientResponse> {
    override val ClientRequest.inject: Request
        get() = Jersey1RequestConverter(this)

    override fun Response.project(requestT: ClientRequest): Attempt<ClientResponse> =
            Jersey1ResponseConverter(requestT, this)
}