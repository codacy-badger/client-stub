package com.github.simy4.stub.jersey1

import com.github.simy4.stub.core.StubHandler
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientRequest
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig

object ClientStub {
    @JvmStatic fun create(handler: StubHandler<ClientRequest, ClientResponse>): Client =
            create(handler, DefaultClientConfig())

    @JvmStatic fun create(handler: StubHandler<ClientRequest, ClientResponse>, clientConfig: ClientConfig): Client =
            Client({ handler(it).run() }, clientConfig)
}
