package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.http.handleErrors
import com.hexagonkt.contact.injector
import com.hexagonkt.contact.services.JwtService
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.http.handlers.HttpHandler
import com.hexagonkt.http.handlers.path
import com.hexagonkt.http.server.callbacks.CorsCallback

internal val router: HttpHandler = path {
    filter("*", CorsCallback())

    path("/user", userRouter)
    path("/contacts", contactsRouter)

    handleErrors()
}

internal fun Router.requireAuthentication() {
    before("/") { authenticate() }
    before("/*") { authenticate() }
}

internal fun Call.authenticate(): User {
    val user = parseUser()
    user ?: halt(401, "Unauthorized")
    return user
}

internal fun Call.parseUser(): User? {
    val userStore: UserStore = injector.inject(UserStore::class)
    val jwtService: JwtService = injector.inject(JwtService::class)

    val token = request.headers["Authorization"]?.firstOrNull() ?: return null

    val decodedJWT = jwtService.verify(token.removePrefix("Token").trim())
    val userId = decodedJWT.subject
    val user = userStore.findById(userId) ?: return null

    attributes["user"] = user

    return user
}
