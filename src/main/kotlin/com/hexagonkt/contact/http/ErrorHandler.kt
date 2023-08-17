package com.hexagonkt.contact.http

import com.hexagonkt.helpers.CodedException
import com.hexagonkt.helpers.MultipleException
import com.hexagonkt.http.model.HttpCall
import com.hexagonkt.http.handlers.HandlerBuilder
import com.hexagonkt.serialization.jackson.json.Json

data class ErrorResponse(val body: List<String> = listOf("Unknown error"))

data class ErrorResponseRoot(val errors: ErrorResponse)

fun HandlerBuilder.handleErrors() {

    setOf(401, 403, 404, 500).forEach { code ->
        error(code) { statusCodeHandler(it) }
    }

    error(MultipleException::class) { multipleExceptionHandler(it) }
    error(Exception::class) { exceptionHandler(it) }
}

internal fun HttpCall.statusCodeHandler(exception: CodedException) {
    @Suppress("MoveVariableDeclarationIntoWhen") // Required because response.body is an expression
    val body = response.body

    val messages = when (body) {
        is List<*> -> body.mapNotNull { it?.toString() }
        else -> listOf(exception.message ?: exception::class.java.name)
    }

    send(exception.code, ErrorResponseRoot(ErrorResponse(messages)), Json, Charsets.UTF_8)
}

internal fun HttpCall.multipleExceptionHandler(error: Exception) {
    if (error is MultipleException) {
        val messages = error.causes.map { it.message ?: "<no message>" }
        send(500, ErrorResponseRoot(ErrorResponse(messages)), Json, Charsets.UTF_8)
    }
}

internal fun HttpCall.exceptionHandler(error: Exception) {
    val errorMessage = error.javaClass.simpleName + ": " + (error.message ?: "<no message>")
    send(500, ErrorResponseRoot(ErrorResponse(listOf(errorMessage))), Json, Charsets.UTF_8)
}
