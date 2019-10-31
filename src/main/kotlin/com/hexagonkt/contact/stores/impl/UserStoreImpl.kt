package com.hexagonkt.contact.stores.impl

import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.store.mongodb.MongoDbStore

class UserStoreImpl : UserStore {
    private val store = MongoDbStore(User::class, User::id, mongoDatabase, "users")

    override fun create(user: User): String {
        return store.insertOne(user)
    }

    override fun findById(id: String): User? {
        return store.findOne(id)
    }

    override fun findByUsername(username: String): User? {
        return store.findOne(mapOf(User::username.name to username))
    }

    override fun deleteById(id: String) {
        store.deleteOne(id)
    }
}
