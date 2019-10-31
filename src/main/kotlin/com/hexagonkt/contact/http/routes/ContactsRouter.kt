package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.injector
import com.hexagonkt.contact.services.JwtService
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.http.server.Router

internal val contactsRouter = Router {
    val userStore: UserStore = injector.inject(UserStore::class)
    val jwtService: JwtService = injector.inject(JwtService::class)

}
