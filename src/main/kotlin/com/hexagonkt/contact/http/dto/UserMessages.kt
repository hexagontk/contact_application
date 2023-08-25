package com.hexagonkt.contact.http.dto

import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.core.fieldsMapOfNotNull
import com.hexagonkt.core.requireMap
import com.hexagonkt.core.requireString
import com.hexagonkt.serialization.Data

data class RegisterRequest(
    val email: String = "",
    val username: String = "",
    val password: String = "",
) : Data<RegisterRequest> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            RegisterRequest::email to email,
            RegisterRequest::username to username,
            RegisterRequest::password to password,
        )

    override fun with(data: Map<String, *>): RegisterRequest =
        RegisterRequest(
            email = data.requireString(RegisterRequest::email),
            username = data.requireString(RegisterRequest::username),
            password = data.requireString(RegisterRequest::password),
        )
}

data class RegisterResponse(val user: UserResponse = UserResponse()) : Data<RegisterResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(RegisterResponse::user to user.data())

    override fun with(data: Map<String, *>): RegisterResponse =
        RegisterResponse(user = user.with(data.requireMap(RegisterResponse::user)))
}

data class LoginRequest(
    val username: String = "",
    val password: String = ""
) : Data<LoginRequest> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            LoginRequest::username to username,
            LoginRequest::password to password,
        )

    override fun with(data: Map<String, *>): LoginRequest =
        LoginRequest(
            username = data.requireString(LoginRequest::username),
            password = data.requireString(LoginRequest::password),
        )
}

data class LoginResponse(val user: UserResponse = UserResponse()) : Data<LoginResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(LoginResponse::user to user.data())

    override fun with(data: Map<String, *>): LoginResponse =
        LoginResponse(user = user.with(data.requireMap(LoginResponse::user)))
}

data class UserResponse(
    val email: String = "",
    val username: String = "",
    val token: String = "",
) : Data<UserResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(
            UserResponse::email to email,
            UserResponse::username to username,
            UserResponse::token to token,
        )

    override fun with(data: Map<String, *>): UserResponse =
        UserResponse(
            email = data.requireString(UserResponse::email),
            username = data.requireString(UserResponse::username),
            token = data.requireString(UserResponse::token),
        )
}

fun User.toUserResponse(token: String) = UserResponse(
    email = this.email,
    username = this.username,
    token = token
)
