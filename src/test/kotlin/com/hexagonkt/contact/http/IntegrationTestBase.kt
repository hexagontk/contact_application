package com.hexagonkt.contact.http

import com.hexagonkt.contact.ApplicationClient
import com.hexagonkt.contact.server
import com.hexagonkt.contact.stores.impl.mongoDatabase
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
open class IntegrationTestBase {

    internal val client by lazy {
        ApplicationClient("http://localhost:${server.runtimePort}/api")
    }

    @BeforeAll
    fun startup() {
        server.start()
    }

    @AfterAll
    fun shutdown() {
        server.stop()
    }

    @AfterEach
    fun cleanUp() {
        mongoDatabase.getCollection("users").deleteMany(Document())
        mongoDatabase.getCollection("contacts").deleteMany(Document())
    }
}
