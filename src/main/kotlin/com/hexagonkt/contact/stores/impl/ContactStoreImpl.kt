package com.hexagonkt.contact.stores.impl

import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.store.mongodb.MongoDbStore

class ContactStoreImpl : ContactStore {
    private val store = MongoDbStore(Contact::class, Contact::id, mongoDatabase, "contacts")

    override fun findAll(): List<Contact> {
        return store.findAll()
    }

    override fun findOne(id: String): Contact? {
        return store.findOne(id)
    }

    override fun insertOne(contact: Contact): String {
        return store.insertOne(contact)
    }

    override fun updateOne(id: String, updates: Map<String, Any?>): Boolean {
        return store.updateOne(id, updates)
    }

    override fun deleteAll(): Boolean {
        return store.deleteMany(emptyMap<String, Any>()) > 0
    }

    override fun deleteOne(id: String): Boolean {
        return store.deleteOne(id)
    }
}
