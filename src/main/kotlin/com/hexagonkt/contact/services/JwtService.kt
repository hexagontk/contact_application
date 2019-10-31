package com.hexagonkt.contact.services

import com.auth0.jwt.interfaces.DecodedJWT

interface JwtService {
    fun sign(subject: String): String

    fun verify(token: String): DecodedJWT
}
