package com.hexagonkt.contact.stores.impl

import com.hexagonkt.helpers.error
import com.hexagonkt.settings.SettingsManager
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

internal val mongoDatabase = MongoClientURI(SettingsManager.requireSetting("mongoDbUrl") as String).let {
    MongoClient(it).getDatabase(it.database ?: error())
}

