package com.hexagonkt.contact

import com.hexagonkt.contact.http.routes.router
import com.hexagonkt.contact.services.JwtServiceImpl
import com.hexagonkt.contact.stores.ContactStore
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.impl.ContactStoreImpl
import com.hexagonkt.contact.stores.impl.UserStoreImpl
import com.hexagonkt.core.Jvm.systemSettingOrNull
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.http.server.HttpServerSettings
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import java.net.InetAddress
import java.net.URL

lateinit var server: HttpServer

fun main() {
    val adapter = JettyServletAdapter()
    val settings = HttpServerSettings(
        bindAddress = (systemSettingOrNull("bindAddress") ?: "0.0.0.0").let(InetAddress::getByName),
        bindPort = systemSettingOrNull("bindPort") ?: 9090,
        contextPath = systemSettingOrNull("bindPort") ?: "/api",
    )
    server = HttpServer(adapter, router, settings)
    server.start()
}

fun createJwtService(): JwtServiceImpl {
    val jwtKeyStore = systemSettingOrNull("jwtKeyStore") ?: "jwt-keys.p12"
    val jwtKeyPassword = systemSettingOrNull("jwtKeyPassword") ?: "jwt-key"
    val jwtKeyAlias = systemSettingOrNull("jwtKeyAlias") ?: "jwt-psw"

    return JwtServiceImpl(URL(jwtKeyStore), jwtKeyPassword, jwtKeyAlias)
}

fun createUserStore(): UserStore =
    UserStoreImpl()

fun createContactStore(): ContactStore =
    ContactStoreImpl()
