package com.hexagonkt.contact.stores

import com.hexagonkt.contact.stores.entities.User

interface UserStore {

    fun create(user: User): String

    fun findById(id: String): User?

    fun findByUsername(username: String): User?

    fun deleteById(id: String): Boolean
}
