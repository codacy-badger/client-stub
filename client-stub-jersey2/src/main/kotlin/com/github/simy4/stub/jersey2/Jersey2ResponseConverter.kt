package com.github.simy4.stub.jersey2

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Response
import org.glassfish.jersey.client.ClientRequest
import org.glassfish.jersey.client.ClientResponse
import org.glassfish.jersey.internal.util.collection.StringKeyIgnoreCaseMultivaluedMap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.ws.rs.ProcessingException
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

internal object Jersey2ResponseConverter : (ClientRequest, Response) -> Attempt<ClientResponse> {

    private val DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_OCTET_STREAM_TYPE

    override fun invoke(request: ClientRequest, response: Response): Attempt<ClientResponse> {
        return Attempt { invoke0(request, response) }
    }

    @Throws(ProcessingException::class)
    private fun invoke0(request: ClientRequest, response: Response): ClientResponse {
        val body = response.body
        val responseHeaders = StringKeyIgnoreCaseMultivaluedMap<String>()
        val responseBody = when (body) {
            null -> {
                responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, "0")
                ByteArray(0)
            }
            is ByteArray -> {
                responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, body.size.toString())
                body
            }
            else -> {
                val os = ByteArrayOutputStream()
                try {
                    ClientRequest(request).apply {
                        setEntity(body, emptyArray(), request.contentType)
                        setStreamProvider { contentLength ->
                            if (contentLength > 0) {
                                responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, contentLength.toString())
                            }
                            os
                        }
                        enableBuffering()
                        writeEntity()
                    }
                    os.toByteArray()
                } catch (io: IOException) {
                    throw ProcessingException("Unable to write response payload", io)
                }
            }
        }
        response.headers.forEach { name, values -> responseHeaders[name] = values.toList() }
        return ClientResponse(javax.ws.rs.core.Response.Status.fromStatusCode(response.status), request).apply {
            entityStream = ByteArrayInputStream(responseBody)
            headers(responseHeaders)
        }
    }

    private val ClientRequest.contentType
        get() = headers.getOrDefault(HttpHeaders.ACCEPT, emptyList()).firstOrNull()
                ?.let { type -> MediaType.valueOf(type.toString()) } ?: DEFAULT_CONTENT_TYPE
}