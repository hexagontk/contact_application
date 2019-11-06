package com.hexagonkt.contact.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.hexagonkt.helpers.Resource
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class JwtServiceImpl(
    keyStoreResource: Resource,
    keyStorePassword: String,
    private val keyAlias: String) : JwtService {

    private val keyStore = loadKeyStore(keyStoreResource, keyStorePassword)
    private val privateKey = this.keyStore.getPrivateKey(keyAlias, keyStorePassword)
    private val publicKey = this.keyStore.getPublicKey(keyAlias)
    private val algorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)
    private val verifier = JWT.require(algorithm).withIssuer(keyAlias).build()

    override fun sign(subject: String): String =
        JWT.create().withIssuer(keyAlias).withSubject(subject).sign(algorithm)

    override fun verify(token: String): DecodedJWT =
        verifier.verify(token)
}

fun loadKeyStore(resource: Resource, password: String): KeyStore =
    KeyStore.getInstance("PKCS12").apply {
        load(resource.requireStream(), password.toCharArray())
    }

fun KeyStore.getPrivateKey(alias: String, password: String): RSAPrivateKey =
    this.getKey(alias, password.toCharArray()) as RSAPrivateKey

fun KeyStore.getPublicKey(alias: String): RSAPublicKey =
    this.getCertificate(alias).publicKey as RSAPublicKey
