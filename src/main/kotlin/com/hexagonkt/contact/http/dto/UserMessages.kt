package com.hexagonkt.contact.http.dto

import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.core.*
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
            email = data.getString(RegisterRequest::email) ?: email,
            username = data.getString(RegisterRequest::username) ?: username,
            password = data.getString(RegisterRequest::password) ?: password,
        )
}

data class RegisterResponse(val user: UserResponse = UserResponse()) : Data<RegisterResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(RegisterResponse::user to user.data())

    override fun with(data: Map<String, *>): RegisterResponse =
        RegisterResponse(user = data.getMap(RegisterResponse::user)?.let(user::with) ?: user)
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
            username = data.getString(LoginRequest::username) ?: username,
            password = data.getString(LoginRequest::password) ?: password,
        )
}

data class LoginResponse(val user: UserResponse = UserResponse()) : Data<LoginResponse> {

    override fun data(): Map<String, *> =
        fieldsMapOfNotNull(LoginResponse::user to user.data())

    override fun with(data: Map<String, *>): LoginResponse =
        LoginResponse(user = data.getMap(LoginResponse::user)?.let(user::with) ?: user)
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
            email = data.getString(UserResponse::email) ?: email,
            username = data.getString(UserResponse::username) ?: username,
            token = data.getString(UserResponse::token) ?: token,
        )
}

fun User.toUserResponse(token: String) = UserResponse(
    email = this.email,
    username = this.username,
    token = token
)
