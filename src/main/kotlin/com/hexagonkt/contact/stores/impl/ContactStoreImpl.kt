package com.hexagonkt.contact.stores.impl

import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.store.mongodb.MongoDbStore

class ContactStoreImpl : ContactStore {
    private val store = MongoDbStore(Contact::class, Contact::id, mongoDatabase, "contacts")

    override fun create(contact: Contact): String {
        return store.insertOne(contact)
    }

    override fun update(id: String, updates: Map<String, Any?>): Boolean {
        return store.updateOne(id, updates)
    }

    override fun findById(id: String): Contact? {
        return store.findOne(id)
    }

    override fun findByUserId(userId: String): List<Contact> {
        return store.findMany(mapOf(Contact::userId.name to userId))
    }

    override fun deleteById(id: String): Boolean {
        return store.deleteOne(id)
    }

    override fun deleteByUserId(userId: String): Long {
        return store.deleteMany(mapOf(Contact::userId.name to userId))
    }
}
