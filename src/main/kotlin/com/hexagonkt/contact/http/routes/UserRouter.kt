package com.hexagonkt.contact.http.routes

import com.hexagonkt.contact.createJwtService
import com.hexagonkt.contact.createUserStore
import com.hexagonkt.contact.http.dto.LoginRequest
import com.hexagonkt.contact.http.dto.RegisterRequest
import com.hexagonkt.contact.http.dto.RegisterResponse
import com.hexagonkt.contact.http.dto.toUserResponse
import com.hexagonkt.contact.services.JwtService
import com.hexagonkt.contact.stores.UserStore
import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.contact.util.HashUtil
import com.hexagonkt.contact.util.HashUtil.hashPassword
import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.http.handlers.path
import com.hexagonkt.http.model.CONFLICT_409
import com.hexagonkt.http.model.CREATED_201
import com.hexagonkt.http.model.ContentType
import kotlin.text.Charsets.UTF_8

internal val userRouter = path {
    val userStore: UserStore = createUserStore()
    val jwtService: JwtService = createJwtService()
    val contentType = ContentType(APPLICATION_JSON, charset = UTF_8)

    // register
    post {
        val (email, username, password) = request.body(RegisterRequest::class)

        val existingUser = userStore.findByUsername(username)
        if (existingUser != null)
            return@post send(CONFLICT_409, "Already registered")

        val user = User(
            email = email,
            username = username,
            password = hashPassword(password)
        )
        userStore.create(user)

        val token = jwtService.sign(user.id)
        val response = RegisterResponse(user.toUserResponse(token))
        send(CREATED_201, response, contentType = contentType)
    }

    // unregister
    delete {
        val user = authenticate()
        userStore.deleteById(user.id)
        ok()
    }

    post("/login") {
        val (username, password) = request.body(LoginRequest::class)
        val user = userStore.findByUsername(username) ?: return@post notFound("User not found")

        if (!HashUtil.checkPassword(password, user.password)) {
            return@post unauthorized("Unauthorized")
        } else {
            val token = jwtService.sign(user.id)
            val response = RegisterResponse(user.toUserResponse(token))
            ok(response, contentType = contentType)
        }
    }
}
