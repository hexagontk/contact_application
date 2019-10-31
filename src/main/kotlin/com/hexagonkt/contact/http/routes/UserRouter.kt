package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.http.dto.*
import com.hexagonkt.contact.injector
import com.hexagonkt.contact.services.JwtService
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.contact.util.HashUtil
import com.hexagonkt.contact.util.HashUtil.hashPassword
import com.hexagonkt.http.server.Router
import com.hexagonkt.serialization.Json
import kotlin.text.Charsets.UTF_8

internal val userRouter = Router {
    val userStore: UserStore = injector.inject(UserStore::class)
    val jwtService: JwtService = injector.inject(JwtService::class)

    // register
    post {
        val (email, username, password) = request.body(RegisterRequest::class)

        val existingUser = userStore.findByUsername(username)
        if (existingUser != null)
            halt(409, "Already registered")

        val user = User(
            email = email,
            username = username,
            password = hashPassword(password)
        )
        userStore.create(user)

        val token = jwtService.sign(user.id)
        val response = RegisterResponse(UserResponse(email, username, token))
        send(201, response, Json, UTF_8)
    }

    // unregister
    delete {
        val user = authenticate()
        userStore.deleteById(user.id)
        ok()
    }

    post("/login") {
        val (username, password) = request.body(LoginRequest::class)
        val user = userStore.findByUsername(username) ?: halt(404, "User not found")

        if (!HashUtil.checkPassword(password, user.password)) {
            send(401, "Unauthorized")
        } else {
            val token = jwtService.sign(user.id)
            val response = LoginResponse(UserResponse(user.email, username, token))
            ok(response, Json, UTF_8)
        }
    }
}
