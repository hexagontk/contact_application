package com.hexagonkt.contact

import com.hexagonkt.contact.http.dto.*
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
        return authenticatedClient ?: client
    }

    private fun onAuthenticated(token: String) {
        val headers = mapOf("Authorization" to listOf("Token $token"))
        authenticatedClient = Client(apiUrl, Json.contentType, headers = headers)
    }

    fun registerUser(user: User) {
        registerUser(user) {
            assertEquals(201, statusCode)

            val response = responseBody.parse(RegisterResponse::class)
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun loginUser(user: User) {
        loginUser(user) {
            assertEquals(200, statusCode)

            val response = responseBody.parse(LoginResponse::class)
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun deleteUser() {
        deleteUser {
            assertEquals(200, statusCode)
        }
    }

    fun withContacts(callback: ContactsResponse.() -> Unit) {
        listContacts {
            assertEquals(200, statusCode)

            val response = responseBody.parse(ContactsResponse::class)
            response.apply(callback)
        }
    }

    fun withContact(contactId: String, callback: ContactResponse.() -> Unit) {
        getContact(contactId) {
            assertEquals(200, statusCode)

            val response = responseBody.parse(ContactResponse::class)
            response.apply(callback)
        }
    }

    fun createContact(contact: ContactRequest): String {
        var contactId = ""
        createContact(contact) {
            assertEquals(201, statusCode)

            val response = responseBody.parse(ContactResponse::class)
            contactId = response.id
        }
        return contactId
    }

    fun updateContact(contactId: String, contact: ContactRequest) {
        updateContact(contactId, contact) {
            assertEquals(200, statusCode)
        }
    }

    fun deleteContact(contactId: String) {
        deleteContact(contactId) {
            assertEquals(200, statusCode)
        }
    }

    fun registerUser(user: User, callback: Response.() -> Unit) {
        client().post("/user", user.toRegisterRequest()).apply(callback)
    }

    fun loginUser(user: User, callback: Response.() -> Unit) {
        client().post("/user/login", user.toLoginRequest()).apply(callback)
    }

    fun deleteUser(callback: Response.() -> Unit) {
        client().delete("/user").apply(callback)
    }

    fun listContacts(callback: Response.() -> Unit) {
        client().get("/contacts").apply(callback)
    }

    fun createContact(contact: ContactRequest, callback: Response.() -> Unit) {
        client().post("/contacts", contact).apply(callback)
    }

    fun getContact(contactId: String, callback: Response.() -> Unit) {
        client().get("/contacts/$contactId").apply(callback)
    }

    fun updateContact(contactId: String, contact: ContactRequest, callback: Response.() -> Unit) {
        client().put("/contacts/$contactId", contact).apply(callback)
    }

    fun deleteContact(contactId: String, callback: Response.() -> Unit) {
        client().delete("/contacts/$contactId").apply(callback)
    }
}
