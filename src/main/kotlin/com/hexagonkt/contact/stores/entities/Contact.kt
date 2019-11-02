package com.hexagonkt.contact.stores.entities

import java.time.LocalDateTime
import java.util.*

data class Contact(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,

    val firstName: String?,
    val middleName: String?,
    val lastName: String?,

    val email: String?,
    val phone: String?,
    val address: String?,
    val note: String?,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = createdAt
)
