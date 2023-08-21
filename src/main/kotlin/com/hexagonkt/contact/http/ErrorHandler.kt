package com.hexagonkt.contact.http

import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.helpers.MultipleException
import com.hexagonkt.http.handlers.HandlerBuilder
import com.hexagonkt.http.handlers.HttpContext
import com.hexagonkt.http.model.*
import java.lang.RuntimeException

data class ErrorResponse(val body: List<String> = listOf("Unknown error"))

data class ErrorResponseRoot(val errors: ErrorResponse)

fun HandlerBuilder.handleErrors() {

    setOf(UNAUTHORIZED_401, FORBIDDEN_403, NOT_FOUND_404, INTERNAL_SERVER_ERROR_500).forEach { code ->
        on(status = code, pattern = "*") { statusCodeHandler() }
    }

    exception<MultipleException> { multipleExceptionHandler(it) }
    exception<Exception> { exceptionHandler(it) }
}

internal fun HttpContext.statusCodeHandler(): HttpContext {
    @Suppress("MoveVariableDeclarationIntoWhen") // Required because response.body is an expression
    val body = response.body

    val messages = when (body) {
        is List<*> -> body.mapNotNull { it?.toString() }
        else -> listOf(exception?.message ?: exception?.let { it::class.java.name} ?: RuntimeException::class.java.name)
    }

    return send(status, ErrorResponseRoot(ErrorResponse(messages)), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
}

internal fun HttpContext.multipleExceptionHandler(error: Exception): HttpContext {
    if (error is MultipleException) {
        val messages = error.causes.map { it.message ?: "<no message>" }
        return send(INTERNAL_SERVER_ERROR_500, ErrorResponseRoot(ErrorResponse(messages)), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
    }
    return this
}

internal fun HttpContext.exceptionHandler(error: Exception): HttpContext {
    val errorMessage = error.javaClass.simpleName + ": " + (error.message ?: "<no message>")
    return send(INTERNAL_SERVER_ERROR_500, ErrorResponseRoot(ErrorResponse(listOf(errorMessage))), contentType = ContentType(APPLICATION_JSON, charset = Charsets.UTF_8))
}
