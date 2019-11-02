package com.hexagonkt.contact.stores

import com.hexagonkt.contact.stores.entities.Contact

interface ContactStore {

    fun create(contact: Contact): String

    fun update(id: String, updates: Map<String, Any?>): Boolean

    fun findById(id: String): Contact?

    fun findByUserId(userId: String): List<Contact>

    fun deleteById(id: String): Boolean

    fun deleteByUserId(userId: String): Long
}
