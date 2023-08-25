package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.createJwtService
import com.hexagonkt.contact.createUserStore
import com.hexagonkt.contact.http.handleErrors
import com.hexagonkt.contact.services.JwtService
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.http.handlers.HttpContext
import com.hexagonkt.http.handlers.HttpHandler
import com.hexagonkt.http.handlers.path
import com.hexagonkt.http.server.callbacks.CorsCallback

internal val router: HttpHandler = path {
    filter("*", CorsCallback())

    path("/user", userRouter)
    path("/contacts", contactsRouter)

    handleErrors()
}

internal fun HttpContext.parseUser(block: (User) -> Unit): HttpContext {
    val userStore: UserStore = createUserStore()
    val jwtService: JwtService = createJwtService()

    val token = request.headers["Authorization"]?.string() ?: return unauthorized("Unauthorized")

    val decodedJWT = jwtService.verify(token.removePrefix("Token").trim())
    val userId = decodedJWT.subject
    val user = userStore.findById(userId) ?: return unauthorized("Unauthorized")

    block(user)

    return send(attributes = this.attributes + ("user" to user))
}
