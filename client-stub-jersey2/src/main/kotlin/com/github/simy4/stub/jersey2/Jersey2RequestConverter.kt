package com.github.simy4.stub.jersey2

import com.github.simy4.stub.core.Request
import org.glassfish.jersey.client.ClientRequest

internal object Jersey2RequestConverter : (ClientRequest) -> Request {

    override fun invoke(clientRequest: ClientRequest): Request {
        val uri = clientRequest.uri
        val query = uri.query?.split('&')
                ?.map { param -> param.indexOf('=').let { idx ->
                    param.substring(0, idx) to param.substring(idx + 1)
                } }
                ?.toMap() ?: emptyMap()
        val headers = clientRequest.headers
                .mapValues { entry -> entry.value.map(Any?::toString) }
        return Request(clientRequest.method, uri.path, query, headers, clientRequest.entity)
    }
}