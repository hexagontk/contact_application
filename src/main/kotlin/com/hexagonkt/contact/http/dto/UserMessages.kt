package com.hexagonkt.contact.http.dto

import com.hexagonkt.contact.stores.entities.User
import com.hexagonkt.serialization.Data

data class RegisterRequest(
    val email: String = "", val username: String = "", val password: String = ""
) : Data<RegisterRequest> {
    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): RegisterRequest {
        TODO("Not yet implemented")
    }
}

data class RegisterResponse(
    val user: UserResponse = UserResponse()
) : Data<RegisterResponse> {

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): RegisterResponse {
        TODO("Not yet implemented")
    }
}

data class LoginRequest(
    val username: String = "", val password: String = ""
) : Data<LoginRequest> {

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): LoginRequest {
        TODO("Not yet implemented")
    }
}

data class LoginResponse(val user: UserResponse = UserResponse()) : Data<LoginResponse> {

    override fun data(): Map<String, *> {
        TODO("Not yet implemented")
    }

    override fun with(data: Map<String, *>): LoginResponse {
        TODO("Not yet implemented")
    }
}

data class UserResponse(
    val email: String = "",
    val username: String = "",
    val token: String = "",
)

fun User.toUserResponse(token: String) = UserResponse(
    email = this.email,
    username = this.username,
    token = token
)
