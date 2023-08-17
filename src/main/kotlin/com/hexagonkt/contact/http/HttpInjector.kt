package com.hexagonkt.contact.http

import com.hexagonkt.contact.http.routes.router
import com.hexagonkt.contact.injector
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.http.server.HttpServerPort
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.injection.Module

fun configureHttpInjection(injectionManager: Module) {
    injectionManager.apply {
        bind(HttpServerPort::class) { JettyServletAdapter() }
        bind(HttpServer::class) {
            val serverPort = injector.inject(HttpServerPort::class)
            HttpServer(serverPort, router)
        }
    }
}
