package com.hexagonkt.contact.http

import com.hexagonkt.contact.stores.entities.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
class UserRouterIntegrationTest : IntegrationTestBase() {

    private val jake = User(
        email = "jake@jake.jake",
        username = "jake",
        password = "jakejake"
    )

    private val jakeWrongPass = User(
        email = "jake@jake.jake",
        username = "jake",
        password = "wrong"
    )

    @Test
    fun `Test registration and login flow`() {

        // user not found
        client.loginUser(jake) {
            assertEquals(404, status.code)
        }

        // register
        client.registerUser(jake)

        // already registered (conflict)
        client.registerUser(jake) {
            assertEquals(409, status.code)
        }

        // invalid password
        client.loginUser(jakeWrongPass) {
            assertEquals(401, status.code)
        }

        // login successfull (2x times)
        client.loginUser(jake)
        client.loginUser(jake)

        //delete user
        client.deleteUser()
        client.deleteUser {
            assertEquals(401, status.code)
        }

        // user not found
        client.loginUser(jake) {
            assertEquals(404, status.code)
        }
    }
}
