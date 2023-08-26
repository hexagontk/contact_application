package com.hexagonkt.contact.http

import com.hexagonkt.core.fieldsMapOfNotNull
import com.hexagonkt.core.logging.Logger
import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.helpers.MultipleException
import com.hexagonkt.http.handlers.HandlerBuilder
import com.hexagonkt.http.handlers.HttpContext
import com.hexagonkt.http.model.*
import com.hexagonkt.serialization.Data
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import java.lang.RuntimeException

private val logger: Logger = Logger("com.hexagonkt.contact.http")

data class ErrorResponse(val body: List<String> = listOf("Unknown error"))

data class ErrorResponseRoot(val errors: ErrorResponse) : Data<ErrorResponseRoot> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            ErrorResponseRoot::errors to fieldsMapOfNotNull(
                ErrorResponse::body to errors.body
            )
        )

    override fun with(data: Map<String, *>): ErrorResponseRoot {
        TODO("Not yet implemented")
    }
}

fun HandlerBuilder.handleErrors() {

    setOf(UNAUTHORIZED_401, FORBIDDEN_403, NOT_FOUND_404, INTERNAL_SERVER_ERROR_500).forEach { code ->
        on(status = code, pattern = "*") { statusCodeHandler() }
    }

    exception<MultipleException> { multipleExceptionHandler(it) }
    exception<Exception> { exceptionHandler(it) }
}

internal fun HttpContext.statusCodeHandler(): HttpContext {

    val messages = when (val body = response.body) {
        is List<*> -> body.mapNotNull { it?.toString() }
        else -> listOf(exception?.message ?: exception?.let { it::class.java.name} ?: RuntimeException::class.java.name)
    }

    return send(status, ErrorResponseRoot(ErrorResponse(messages)).data().serialize(Json), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
}

internal fun HttpContext.multipleExceptionHandler(error: Exception): HttpContext {
    logger.error(error)
    if (error is MultipleException) {
        val messages = error.causes.map { it.message ?: "<no message>" }
        return send(INTERNAL_SERVER_ERROR_500, ErrorResponseRoot(ErrorResponse(messages)).data().serialize(Json), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
    }
    return this
}

internal fun HttpContext.exceptionHandler(error: Exception): HttpContext {
    logger.error(error)
    val errorMessage = error.javaClass.simpleName + ": " + (error.message ?: "<no message>")
    return send(INTERNAL_SERVER_ERROR_500, ErrorResponseRoot(ErrorResponse(listOf(errorMessage))).data().serialize(Json), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
}
