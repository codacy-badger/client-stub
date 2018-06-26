package com.github.simy4.stub.jersey2.connector

import com.github.simy4.stub.core.StubHandler
import org.glassfish.jersey.client.ClientRequest
import org.glassfish.jersey.client.ClientResponse
import org.glassfish.jersey.client.spi.AsyncConnectorCallback
import org.glassfish.jersey.client.spi.Connector
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class StubConnector(
    private val stubHandler: StubHandler<ClientRequest, ClientResponse>,
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
) : Connector {
    override fun apply(request: ClientRequest): ClientResponse = stubHandler(request).run()

    override fun apply(request: ClientRequest, callback: AsyncConnectorCallback): Future<*> = executor.submit {
        stubHandler(request)
                .doOnSuccess { callback.response(it) }
                .doOnError { callback.failure(it) }
    }

    override fun getName(): String = "Stub Connector"

    override fun close() {
        executor.shutdown()
    }
}