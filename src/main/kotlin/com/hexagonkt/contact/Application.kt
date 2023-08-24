package com.hexagonkt.contact

import com.hexagonkt.contact.http.routes.router
import com.hexagonkt.contact.services.JwtServiceImpl
import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.impl.ContactStoreImpl
import com.hexagonkt.contact.stores.impl.UserStoreImpl
import com.hexagonkt.core.Jvm
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import java.net.URL

fun main() {
    val adapter = JettyServletAdapter()
    val server = HttpServer(adapter, router)
    server.start()
}

fun createJwtService(): JwtServiceImpl {
    val jwtKeyStore = Jvm.systemSettingOrNull("jwtKeyStore") ?: "jwt-keys.p12"
    val jwtKeyPassword = Jvm.systemSettingOrNull("jwtKeyPassword") ?: "jwt-key"
    val jwtKeyAlias = Jvm.systemSettingOrNull("jwtKeyAlias") ?: "jwt-psw"

    return JwtServiceImpl(URL(jwtKeyStore), jwtKeyPassword, jwtKeyAlias)
}

fun createUserStore(): UserStore =
    UserStoreImpl()

fun createContactStore(): ContactStore =
    ContactStoreImpl()
