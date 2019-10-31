package com.hexagonkt.contact.stores

import com.hexagonkt.contact.stores.entities.Contact

interface ContactStore {

    fun findAll(): List<Contact>

    fun findOne(id: String): Contact?

    fun insertOne(contact: Contact): String

    fun updateOne(id: String, updates: Map<String, Any?>): Boolean

    fun deleteAll(): Boolean

    fun deleteOne(id: String): Boolean

}
