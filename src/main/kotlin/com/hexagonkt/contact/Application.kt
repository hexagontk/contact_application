package com.hexagonkt.contact

import com.hexagonkt.contact.http.configureHttpInjection
import com.hexagonkt.contact.services.configureServicesInjection
import com.hexagonkt.contact.stores.configureStoresInjection
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.injection.Injector
import com.hexagonkt.injection.Module

internal val injector = Module()
    .apply {
        configureStoresInjection(this)
        configureServicesInjection(this)
        configureHttpInjection(this)
    }
    .let(::Injector)

fun main() {
    injector
        .inject(HttpServer::class)
        .start()
}
