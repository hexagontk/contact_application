package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.createContactStore
import com.hexagonkt.contact.http.dto.ContactRequest
import com.hexagonkt.contact.http.dto.ContactsResponse
import com.hexagonkt.contact.http.dto.toContactResponse
import com.hexagonkt.contact.http.dto.toUpdatesMap
import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.core.require
import com.hexagonkt.http.handlers.HttpContext
import com.hexagonkt.http.handlers.path
import com.hexagonkt.http.model.CREATED_201
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.parseMap
import com.hexagonkt.serialization.serialize
import com.hexagonkt.serialization.toData
import kotlin.text.Charsets.UTF_8

internal val contactsRouter = path {
    val contactStore: ContactStore = createContactStore()
    val contentType = ContentType(APPLICATION_JSON, charset = UTF_8)

    filter("*") { parseUser { next() } }

    // list
    get {
        val user = attributes["user"] as User
        val contacts = contactStore.findByUserId(user.id)

        val response = ContactsResponse(
            contacts.map { it.toContactResponse() }
        )
        ok(response.serialize(Json), contentType = contentType)
    }

    // create
    post {
        val user = attributes["user"] as User

        val contactRequest = request.bodyString().parseMap(Json).toData(::ContactRequest)

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

        send(CREATED_201, contact.toContactResponse().serialize(Json), contentType = contentType)
    }

    // get
    get("/{contactId}") {
        val contact = requireContact(contactStore) ?: return@get notFound("Contact not found")
        ok(contact.toContactResponse().serialize(Json), contentType = contentType)
    }

    // update
    put("/{contactId}") {
        val contact = requireContact(contactStore) ?: return@put notFound("Contact not found")
        val contactRequest = request.bodyString().parseMap(Json).toData(::ContactRequest)

        contactStore.update(contact.id, contactRequest.toUpdatesMap())

        // re-read from db
        val updated = requireContact(contactStore) ?: return@put notFound("Contact not found")
        ok(updated.toContactResponse().serialize(Json), contentType = contentType)
    }

    //delete
    delete("/{contactId}") {
        val contact = requireContact(contactStore) ?: return@delete notFound("Contact not found")
        contactStore.deleteById(contact.id)
        ok()
    }
}

private fun HttpContext.requireContact(contactStore: ContactStore): Contact? {
    val contactId = pathParameters.require("contactId")
    return contactStore.findById(contactId)
}
