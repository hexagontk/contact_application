package com.hexagonkt.contact

import com.hexagonkt.contact.http.configureHttpInjection
import com.hexagonkt.contact.services.configureServicesInjection
import com.hexagonkt.contact.stores.configureStoresInjection
import com.hexagonkt.http.server.Server
import com.hexagonkt.injection.InjectionManager

internal val injector = InjectionManager {
    configureStoresInjection(this)
    configureServicesInjection(this)
    configureHttpInjection(this)
}

fun main() {

    injector
        .inject(Server::class)
        .start()
}
