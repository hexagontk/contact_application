package com.hexagonkt.contact

import com.hexagonkt.contact.http.routes.router
import com.hexagonkt.contact.services.JwtServiceImpl
import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.Contact
import com.hexagonkt.contact.stores.impl.ContactStoreImpl
import com.hexagonkt.contact.stores.impl.UserStoreImpl
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.converters.ConvertersManager
import com.hexagonkt.core.Jvm.systemSettingOrNull
import com.hexagonkt.core.fieldsMapOfNotNull
import com.hexagonkt.core.getString
import com.hexagonkt.core.requireString
import com.hexagonkt.core.urlOf
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.http.server.HttpServerSettings
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import java.net.InetAddress
import java.time.LocalDateTime

lateinit var server: HttpServer

fun main() {
    val adapter = JettyServletAdapter()
    val settings = HttpServerSettings(
        bindAddress = (systemSettingOrNull("bindAddress") ?: "0.0.0.0").let(InetAddress::getByName),
        bindPort = systemSettingOrNull("bindPort") ?: 9090,
        contextPath = systemSettingOrNull("contextPath") ?: "/api",
    )
    server = HttpServer(adapter, router, settings)
    server.start()
}

fun createJwtService(): JwtServiceImpl {
    val jwtKeyStore = systemSettingOrNull("jwtKeyStore") ?: "classpath:jwt-keys.p12"
    val jwtKeyPassword = systemSettingOrNull("jwtKeyPassword") ?: "21p.tknogaxeh"
    val jwtKeyAlias = systemSettingOrNull("jwtKeyAlias") ?: "hexagonkt"

    return JwtServiceImpl(urlOf(jwtKeyStore), jwtKeyPassword, jwtKeyAlias)
}

fun createUserStore(): UserStore {

    ConvertersManager.register(User::class to Map::class) {
        fieldsMapOfNotNull(
            User::id to it.id,
            User::email to it.email,
            User::username to it.username,
            User::password to it.password,
        )
    }

    ConvertersManager.register(Map::class to User::class) {
        User(
            id = it.requireString(User::id),
            email = it.requireString(User::email),
            username = it.requireString(User::username),
            password = it.requireString(User::password),
        )
    }

    return UserStoreImpl()
}

fun createContactStore(): ContactStore {

    ConvertersManager.register(Contact::class to Map::class) {
        fieldsMapOfNotNull(
            Contact::id to it.id,
            Contact::userId to it.userId,

            Contact::firstName to it.firstName,
            Contact::middleName to it.middleName,
            Contact::lastName to it.lastName,

            Contact::email to it.email,
            Contact::phone to it.phone,
            Contact::address to it.address,
            Contact::note to it.note,

            Contact::createdAt to it.createdAt.toString(),
            Contact::updatedAt to it.updatedAt.toString(),
        )
    }

    ConvertersManager.register(Map::class to Contact::class) {
        Contact(
            id = it.requireString(Contact::id),
            userId = it.requireString(Contact::userId),

            firstName = it.getString(Contact::firstName),
            middleName = it.getString(Contact::middleName),
            lastName = it.getString(Contact::lastName),

            email = it.getString(Contact::email),
            phone = it.getString(Contact::phone),
            address = it.getString(Contact::address),
            note = it.getString(Contact::note),

            createdAt = it.requireString(Contact::createdAt).let(LocalDateTime::parse),
            updatedAt = it.requireString(Contact::updatedAt).let(LocalDateTime::parse),
        )
    }

    return ContactStoreImpl()
}
