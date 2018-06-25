package com.github.simy4.stub.jersey2

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Request
import com.github.simy4.stub.core.Response
import com.github.simy4.stub.core.StubHandlerSupport
import org.glassfish.jersey.client.ClientRequest
import org.glassfish.jersey.client.ClientResponse

interface Jersey2StubHandlerSupplort: StubHandlerSupport<ClientRequest, ClientResponse> {
    override val ClientRequest.inject: Request
        get() = Jersey2RequestConverter(this)

    override fun Response.project(requestT: ClientRequest): Attempt<ClientResponse> =
            Jersey2ResponseConverter(requestT, this)
}