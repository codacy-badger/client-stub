package com.github.simy4.stub.jersey2

import com.github.simy4.stub.core.StubHandler
import com.github.simy4.stub.jersey2.connector.StubConnector
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.client.ClientRequest
import org.glassfish.jersey.client.ClientResponse
import org.glassfish.jersey.client.JerseyClient
import org.glassfish.jersey.client.JerseyClientBuilder
import javax.ws.rs.core.Configuration

object ClientStub {
    @JvmStatic fun create(handler: StubHandler<ClientRequest, ClientResponse>): JerseyClient =
            create(handler, ClientConfig())

    @JvmStatic fun create(handler: StubHandler<ClientRequest, ClientResponse>, configurations: Configuration): JerseyClient {
        val clientConfig = ClientConfig()
        clientConfig.loadFrom(configurations)
        clientConfig.connectorProvider { _, _ -> StubConnector(handler) }
        return JerseyClientBuilder.createClient(clientConfig).preInitialize()
    }
}
