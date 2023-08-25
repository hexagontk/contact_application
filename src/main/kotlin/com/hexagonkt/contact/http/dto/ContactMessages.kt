package com.hexagonkt.contact.http.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.converters.convert
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

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): ContactRequest {
        TODO("Not yet implemented")
    }
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

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): ContactResponse {
        TODO("Not yet implemented")
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactsResponse(
    val contacts: List<ContactResponse> = emptyList()
) : Data<ContactsResponse> {

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): ContactsResponse {
        TODO("Not yet implemented")
    }
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
