package com.hexagonkt.contact.stores.entities

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val username: String,
    val password: String
)
