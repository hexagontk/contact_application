package com.hexagonkt.contact

import com.hexagonkt.contact.http.dto.*
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.core.logging.info
import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.http.client.HttpClient
import com.hexagonkt.http.client.HttpClientSettings
import com.hexagonkt.http.client.jetty.JettyClientAdapter
import com.hexagonkt.http.model.*
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.parseMap
import com.hexagonkt.serialization.serialize
import com.hexagonkt.serialization.toData
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ApplicationClient(val apiUrl: String) {
    private val client: HttpClient = HttpClient(
        JettyClientAdapter(),
        HttpClientSettings(URL(apiUrl), contentType = ContentType(APPLICATION_JSON))
    ).apply { start() }

    private var authenticatedClient: HttpClient? = null

    private fun User.toRegisterRequest(): RegisterRequest =
        RegisterRequest(email, username, password)

    private fun User.toLoginRequest(): LoginRequest =
        LoginRequest(username, password)

    private fun client(): HttpClient {
        return authenticatedClient?.apply { if (!started()) start() } ?: client
    }

    private fun onAuthenticated(token: String) {
        val headers = Headers(Header("Authorization", "Token $token"))
        authenticatedClient = HttpClient(JettyClientAdapter(), HttpClientSettings(URL(apiUrl), headers = headers, contentType = ContentType(APPLICATION_JSON)))
    }

    fun registerUser(user: User) {
        registerUser(user) {
            assertEquals(201, status.code)

            val response = bodyString().parseMap(Json).toData(::RegisterResponse)
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun loginUser(user: User) {
        loginUser(user) {
            assertEquals(200, status.code)

            val response = bodyString().parseMap(Json).toData(::LoginResponse)
            assertEquals(user.username, response.user.username)
            assertEquals(user.email, response.user.email)
            assertTrue(response.user.token.isNotBlank())

            onAuthenticated(response.user.token)
        }
    }

    fun deleteUser() {
        deleteUser {
            assertEquals(200, status.code)
        }
    }

    fun withContacts(callback: ContactsResponse.() -> Unit) {
        listContacts {
            assertEquals(200, status.code)

            val response = bodyString().parseMap(Json).toData(::ContactsResponse)
            response.apply(callback)
        }
    }

    fun withContact(contactId: String, callback: ContactResponse.() -> Unit) {
        getContact(contactId) {
            assertEquals(200, status.code)

            val response = bodyString().parseMap(Json).toData(::ContactResponse)
            response.apply(callback)
        }
    }

    fun createContact(contact: ContactRequest): String {
        var contactId = ""
        createContact(contact) {
            assertEquals(201, status.code)

            val response = bodyString().parseMap(Json).toData(::ContactResponse)
            contactId = response.id
        }
        return contactId
    }

    fun updateContact(contactId: String, contact: ContactRequest) {
        updateContact(contactId, contact) {
            assertEquals(200, status.code)
        }
    }

    fun deleteContact(contactId: String) {
        deleteContact(contactId) {
            assertEquals(200, status.code)
        }
    }

    fun registerUser(user: User, callback: HttpResponsePort.() -> Unit) {
        client().post("/user", user.toRegisterRequest().data().serialize(Json)).apply(callback)
    }

    fun loginUser(user: User, callback: HttpResponsePort.() -> Unit) {
        client().post("/user/login", user.toLoginRequest().data().serialize(Json)).apply(callback)
    }

    fun deleteUser(callback: HttpResponsePort.() -> Unit) {
        client().delete("/user").apply(callback)
    }

    fun listContacts(callback: HttpResponsePort.() -> Unit) {
        client().get("/contacts").apply(callback)
    }

    fun createContact(contact: ContactRequest, callback: HttpResponsePort.() -> Unit) {
        client().post("/contacts", contact.data().serialize(Json)).apply(callback)
    }

    fun getContact(contactId: String, callback: HttpResponsePort.() -> Unit) {
        client().get("/contacts/$contactId").apply(callback)
    }

    fun updateContact(contactId: String, contact: ContactRequest, callback: HttpResponsePort.() -> Unit) {
        client().put("/contacts/$contactId", contact.data().serialize(Json)).apply(callback)
    }

    fun deleteContact(contactId: String, callback: HttpResponsePort.() -> Unit) {
        client().delete("/contacts/$contactId").apply(callback)
    }
}
