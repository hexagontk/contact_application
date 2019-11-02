package com.hexagonkt.contact.http

import com.hexagonkt.contact.http.dto.ContactRequest
import com.hexagonkt.contact.stores.entities.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(PER_CLASS)
class ContactsRouterIntegrationTest : IntegrationTestBase() {
    private val jake = User(
        email = "jake@jake.jake",
        username = "jake",
        password = "jakejake"
    )

    private val contact = ContactRequest(
        firstName = "First",
        middleName = "Middle",
        lastName = "Last",

        email = "first@first.first",
        phone = "555-555",
        address = "First Drive, 123",
        note = "note this"
    )

    private val contact2 = ContactRequest(
        firstName = "Second",
        middleName = "Middle",
        lastName = "Last",

        email = "second@second.second",
        phone = "555-555",
        address = "Second Drive, 123",
        note = "note this"
    )

    @Test
    fun `Test working with contacts flow`() {

        // create user
        client.registerUser(jake)
        client.withContacts {
            assertEquals(0, contacts.size)
        }

        // validate create/get
        val contactId = client.createContact(contact)
        client.withContact(contactId) {
            assertEquals(contactId, id)
            assertEquals(contact.firstName, firstName)
            assertEquals(contact.middleName, middleName)
            assertEquals(contact.lastName, lastName)

            assertEquals(contact.email, email)
            assertEquals(contact.phone, phone)
            assertEquals(contact.address, address)
            assertEquals(contact.note, note)

            assertNotNull(createdAt)
            assertEquals(createdAt, updatedAt)
        }

        // validate list
        client.withContacts {
            assertEquals(1, contacts.size)

            val contactResponse = contacts[0]
            assertEquals(contactId, contactResponse.id)
            assertEquals(contact.firstName, contactResponse.firstName)
            assertEquals(contact.middleName, contactResponse.middleName)
            assertEquals(contact.lastName, contactResponse.lastName)

            assertEquals(contact.email, contactResponse.email)
            assertEquals(contact.phone, contactResponse.phone)
            assertEquals(contact.address, contactResponse.address)
            assertEquals(contact.note, contactResponse.note)

        }

        // validate update
        client.updateContact(contactId, ContactRequest(
            email = "new email",
            phone = "new phone",
            address = "new address",
            note = "new note"
        ))

        client.withContact(contactId) {
            assertEquals(contactId, id)
            assertEquals(contact.firstName, firstName)
            assertEquals(contact.middleName, middleName)
            assertEquals(contact.lastName, lastName)

            assertEquals("new email", email)
            assertEquals("new phone", phone)
            assertEquals("new address", address)
            assertEquals("new note", note)
        }

        // validate list multiple contacts
        val contactId2 = client.createContact(contact2)
        client.withContacts {
            assertEquals(2, contacts.size)
            assertTrue(
                contactId == contacts[0].id && contactId2 == contacts[1].id ||
                    contactId == contacts[1].id && contactId2 == contacts[0].id
            )
        }

        // validate delete
        client.deleteContact(contactId)
        client.withContacts {
            assertEquals(1, contacts.size)
            assertEquals(contactId2, contacts[0].id)
        }
    }
}
