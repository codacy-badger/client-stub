package com.github.simy4.stub.jersey1

import com.github.simy4.stub.core.Attempt
import com.github.simy4.stub.core.Response
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientHandlerException
import com.sun.jersey.api.client.ClientRequest
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.core.header.InBoundHeaders
import com.sun.jersey.core.util.StringKeyIgnoreCaseMultivaluedMap
import com.sun.jersey.spi.MessageBodyWorkers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.reflect.Type
import javax.ws.rs.core.GenericEntity
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap

internal object Jersey1ResponseConverter {

    private val DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_OCTET_STREAM_TYPE

    operator fun invoke(request: ClientRequest, response: Response): Attempt<ClientResponse> {
        return Attempt { invoke0(request, response) }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(ClientHandlerException::class)
    private fun invoke0(request: ClientRequest, response: Response): ClientResponse {
        val body = response.body
        val messageBodyWorkers = request.messageBodyWorkers
        val responseHeaders = StringKeyIgnoreCaseMultivaluedMap<Any>()
        val responseBody = when (body) {
            null -> {
                responseHeaders.putSingle(HttpHeaders.CONTENT_TYPE, request.contentType)
                responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, 0)
                ByteArray(0)
            }
            is ByteArray -> {
                responseHeaders.putSingle(HttpHeaders.CONTENT_TYPE, request.contentType)
                responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, body.size)
                body
            }
            else -> {
                val os = ByteArrayOutputStream()
                try {
                    val responseType: Class<*>
                    val genericType: Type?
                    val responseObject: Any?
                    when (body) {
                        is GenericEntity<*> -> {
                            responseType = body.rawType
                            genericType = body.type
                            responseObject = body.entity
                        }
                        else -> {
                            responseType = body.javaClass
                            genericType = null
                            responseObject = body
                        }
                    }
                    val contentType = messageBodyWorkers.writeTo(responseObject, responseType as Class<Any?>, genericType, request.headers, os)
                    val responseBody = os.toByteArray()
                    responseHeaders.putSingle(HttpHeaders.CONTENT_TYPE, contentType)
                    responseHeaders.putSingle(HttpHeaders.CONTENT_LENGTH, responseBody.size)
                    responseBody
                } catch (io: IOException) {
                    throw ClientHandlerException("Unable to write response payload", io)
                } finally {
                    os.close()
                }
            }
        }
        val inBoundHeaders = InBoundHeaders()
        responseHeaders.forEach { name, values -> inBoundHeaders[name] = values.map(ClientRequest::getHeaderValue) }
        response.headers.forEach { name, values -> inBoundHeaders[name] = values.toList() }
        return ClientResponse(response.status, inBoundHeaders, ByteArrayInputStream(responseBody), messageBodyWorkers)
    }

    private val ClientRequest.contentType
        get() = headers.getOrDefault(HttpHeaders.ACCEPT, emptyList()).firstOrNull() ?: DEFAULT_CONTENT_TYPE

    private val ClientRequest.messageBodyWorkers: MessageBodyWorkers
        get() = properties[Client::class.java.canonicalName].let {
            when(it) {
                is Client -> it.messageBodyWorkers
                else -> throw ClientHandlerException("Unable to find message body workers in request context")
            }
        }

    @Throws(ClientHandlerException::class, IOException::class)
    private fun <T> MessageBodyWorkers.writeTo(t: T?, type: Class<T>, genericType: Type?,
                                               headers: MultivaluedMap<String, Any>, outputStream: OutputStream): MediaType {
        val acceptedMediaTypes = headers.getOrDefault(HttpHeaders.ACCEPT, listOf(DEFAULT_CONTENT_TYPE))
                .map { value -> MediaType.valueOf(value.toString()) }
        val contentType = getMessageBodyWriterMediaType(type, genericType, emptyArray(), acceptedMediaTypes)
                ?: throw ClientHandlerException("Unable to find message body writer for: $t")
        val messageBodyWriter = getMessageBodyWriter(type, genericType, emptyArray(), contentType)
        messageBodyWriter.writeTo(t, type, genericType, emptyArray(), contentType, headers, outputStream)
        return contentType
    }

}