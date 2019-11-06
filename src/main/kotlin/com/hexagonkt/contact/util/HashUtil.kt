package com.hexagonkt.contact.util

import at.favre.lib.crypto.bcrypt.BCrypt

object HashUtil {
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(10, password.toCharArray())
    }

    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer()
            .verify(password.toCharArray(), hashedPassword).verified
    }
}
