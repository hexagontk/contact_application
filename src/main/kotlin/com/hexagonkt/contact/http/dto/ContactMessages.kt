package com.hexagonkt.contact.http.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.converters.convert
import java.time.LocalDateTime

data class ContactRequest(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,

    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val note: String? = null
)

fun ContactRequest.toUpdatesMap(): Map<String, Any?> {
    val updatedAt = LocalDateTime.now()
    val updatedAtPair = Contact::updatedAt.name to updatedAt

    return this.convert<Map<*, *>>().mapKeys { it.key.toString() } + updatedAtPair
}

data class ContactResponse(
    val id: String,

    val firstName: String?,
    val middleName: String?,
    val lastName: String?,

    val email: String?,
    val phone: String?,
    val address: String?,
    val note: String?,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContactsResponse(
    val contacts: List<ContactResponse>
)

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
