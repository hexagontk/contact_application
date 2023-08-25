package com.hexagonkt.contact.http.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.converters.convert
import com.hexagonkt.core.fieldsMapOfNotNull
import com.hexagonkt.core.getMapsOrEmpty
import com.hexagonkt.core.getString
import com.hexagonkt.core.requireString
import com.hexagonkt.serialization.Data
import java.time.LocalDateTime

data class ContactRequest(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,

    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val note: String? = null
) : Data<ContactRequest> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            ContactRequest::firstName to firstName,
            ContactRequest::middleName to middleName,
            ContactRequest::lastName to lastName,

            ContactRequest::email to email,
            ContactRequest::phone to phone,
            ContactRequest::address to address,
            ContactRequest::note to note,
        )

    override fun with(data: Map<String, *>): ContactRequest =
        ContactRequest(
            firstName = data.getString(ContactRequest::firstName),
            middleName = data.getString(ContactRequest::middleName),
            lastName = data.getString(ContactRequest::lastName),

            email = data.getString(ContactRequest::email),
            phone = data.getString(ContactRequest::phone),
            address = data.getString(ContactRequest::address),
            note = data.getString(ContactRequest::note),
        )
}

fun ContactRequest.toUpdatesMap(): Map<String, Any?> {
    val updatedAt = LocalDateTime.now()
    val updatedAtPair = Contact::updatedAt.name to updatedAt

    return this.convert<Map<*, *>>().mapKeys { it.key.toString() } + updatedAtPair
}

data class ContactResponse(
    val id: String = "",

    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,

    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val note: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) : Data<ContactResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            ContactResponse::id to id,

            ContactResponse::firstName to firstName,
            ContactResponse::middleName to middleName,
            ContactResponse::lastName to lastName,

            ContactResponse::email to email,
            ContactResponse::phone to phone,
            ContactResponse::address to address,
            ContactResponse::note to note,
        )

    override fun with(data: Map<String, *>): ContactResponse =
        ContactResponse(
            id = data.requireString(ContactResponse::id),

            firstName = data.getString(ContactResponse::firstName),
            middleName = data.getString(ContactResponse::middleName),
            lastName = data.getString(ContactResponse::lastName),

            email = data.getString(ContactResponse::email),
            phone = data.getString(ContactResponse::phone),
            address = data.getString(ContactResponse::address),
            note = data.getString(ContactResponse::note),
        )
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactsResponse(
    val contacts: List<ContactResponse> = emptyList()
) : Data<ContactsResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(ContactsResponse::contacts to contacts.map { it.data() })

    override fun with(data: Map<String, *>): ContactsResponse =
        ContactsResponse(
            contacts =
                data.getMapsOrEmpty(ContactsResponse::contacts).map { ContactResponse().with(it) }
        )
}

fun Contact.toContactResponse() = ContactResponse(
    id = this.id,

    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,

    email = this.email,
    phone = this.phone,
    address = this.address,
    note = this.note,

    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
