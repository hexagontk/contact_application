package com.hexagonkt.contact

import com.hexagonkt.contact.http.dto.LoginRequest
import com.hexagonkt.contact.http.dto.LoginResponse
import com.hexagonkt.contact.http.dto.RegisterRequest
import com.hexagonkt.contact.http.dto.RegisterResponse
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.http.client.Client
import com.hexagonkt.serialization.Json
import com.hexagonkt.serialization.parse
import org.asynchttpclient.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ApplicationClient(val apiUrl: String) {
    private val client: Client = Client(apiUrl, Json.contentType)
    private var authenticatedClient: Client? = null

    private fun User.toRegisterRequest(): RegisterRequest =
        RegisterRequest(email, username, password)

    private fun User.toLoginRequest(): LoginRequest =
        LoginRequest(username, password)

    private fun client(): Client {
        return authenticatedClient ?: client;
    }

    private fun onAuthenticated(token: String) {
        val headers = mapOf("Authorization" to listOf("Token $token"))
        authenticatedClient = Client(apiUrl, Json.contentType, headers = headers)
    }

    fun registerUser(user: User) {
        registerUser(user) {
            assertEquals(201, statusCode)

            val response = responseBody.parse<RegisterResponse>()
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun registerUser(user: User, callback: Response.() -> Unit) {
        client.post("/user", user.toRegisterRequest()).apply(callback)
    }

    fun loginUser(user: User) {
        loginUser(user) {
            assertEquals(200, statusCode)

            val response = responseBody.parse<LoginResponse>()
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun deleteUser() {
        client().delete("/user") {
            assertEquals(200, statusCode)
        }
    }

    fun loginUser(user: User, callback: Response.() -> Unit) {
        client().post("/user/login", user.toLoginRequest()).apply(callback)
    }
}
