package com.hexagonkt.contact

import com.hexagonkt.http.client.Client
import com.hexagonkt.http.server.Server
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest {

    private val server by lazy {
        injector.inject(Server::class)
    }

    private val client by lazy { Client("http://localhost:${server.runtimePort}") }

    @BeforeAll
    fun startup() {
        server.start();
    }

    @AfterAll
    fun shutdown() {
        server.stop()
    }

    @Test
    fun `HTTP request returns the correct body`() {
        val response = client.get("/hello/World")
        val content = response.responseBody

        assert(response.headers["Date"] != null)
        assert(response.headers["Content-Type"] == "text/plain")

        assert("Hello, World!" == content)
    }
}
