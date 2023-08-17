package com.hexagonkt.contact.stores.impl

import com.hexagonkt.core.Jvm
import com.mongodb.client.MongoClients
import java.net.URL

internal val mongoDatabase = Jvm.systemSetting<String>("mongoDbUrl").let {
    MongoClients.create(it).getDatabase(URL(it).path)
}
