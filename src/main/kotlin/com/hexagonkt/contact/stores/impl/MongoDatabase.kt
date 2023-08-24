package com.hexagonkt.contact.stores.impl

import com.hexagonkt.core.Jvm
import com.mongodb.client.MongoClients
import java.net.URL

internal val mongoDatabase =
    (Jvm.systemSettingOrNull("mongoDbUrl") ?: "mongodb://localhost:27017/contacts")
        .let { MongoClients.create(it).getDatabase(URL(it).path) }
