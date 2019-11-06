package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.http.dto.ContactRequest
import com.hexagonkt.contact.http.dto.ContactsResponse
import com.hexagonkt.contact.http.dto.toContactResponse
import com.hexagonkt.contact.http.dto.toUpdatesMap
import com.hexagonkt.contact.injector
import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.http.server.Call
import com.hexagonkt.http.server.Router
import com.hexagonkt.serialization.Json

internal val contactsRouter = Router {
    val contactStore: ContactStore = injector.inject(ContactStore::class)

    requireAuthentication()

    // list
    get {
        val user = attributes["user"] as User
        val contacts = contactStore.findByUserId(user.id)

        val response = ContactsResponse(
            contacts.map { it.toContactResponse() }
        )
        ok(response, Json, Charsets.UTF_8)
    }

    // create
    post {
        val user = attributes["user"] as User

        val contactRequest = request.body(ContactRequest::class)

        val contact = Contact(
            userId = user.id,

            firstName = contactRequest.firstName,
            middleName = contactRequest.middleName,
            lastName = contactRequest.lastName,

            email = contactRequest.email,
            phone = contactRequest.phone,
            address = contactRequest.address,
            note = contactRequest.note
        )

        contactStore.create(contact)

        send(201, contact.toContactResponse(), Json, Charsets.UTF_8)
    }

    // get
    get("/{contactId}") {
        val contact = requireContact(contactStore)
        ok(contact.toContactResponse(), Json, Charsets.UTF_8)
    }

    // update
    put("/{contactId}") {
        val contact = requireContact(contactStore)
        val contactRequest = request.body(ContactRequest::class)

        contactStore.update(contact.id, contactRequest.toUpdatesMap())

        // re-read from db
        val udpated = requireContact(contactStore)
        ok(udpated.toContactResponse(), Json, Charsets.UTF_8)
    }

    //delete
    delete("/{contactId}") {
        val contact = requireContact(contactStore)
        contactStore.deleteById(contact.id)
        ok()
    }
}

private fun Call.requireContact(contactStore: ContactStore): Contact {
    val contactId = pathParameters["contactId"]
    return contactStore.findById(contactId) ?: halt(404, "Contact not found")
}
