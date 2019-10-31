package com.hexagonkt.contact.http.dto

data class RegisterRequest(val email: String, val username: String, val password: String)
data class RegisterResponse(val user: UserResponse)

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val user: UserResponse)

data class UserResponse(val email: String, val username: String, val token: String)
