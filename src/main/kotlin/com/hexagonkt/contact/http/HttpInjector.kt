package com.hexagonkt.contact.http

import com.hexagonkt.contact.http.routes.router
import com.hexagonkt.contact.injector
import com.hexagonkt.http.server.Server
import com.hexagonkt.http.server.ServerPort
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.InjectionManager
import com.hexagonkt.settings.SettingsManager.settings

fun configureHttpInjection(injectionManager: InjectionManager) {
    injectionManager {
        bind(ServerPort::class) { JettyServletAdapter() }
        bind(Server::class) {
            val serverPort = injector.inject(ServerPort::class);
            Server(serverPort, router, settings)
        }
    }
}
