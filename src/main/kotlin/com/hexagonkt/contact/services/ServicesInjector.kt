package com.hexagonkt.contact.services

import com.hexagonkt.core.Jvm
import com.hexagonkt.injection.Module
import java.net.URL

fun configureServicesInjection(injectionManager: Module) {
    injectionManager.apply {
        bind(JwtService::class) {
            val jwtKeyStore = Jvm.systemSetting<String>("jwtKeyStore")
            val jwtKeyPassword = Jvm.systemSetting<String>("jwtKeyPassword")
            val jwtKeyAlias = Jvm.systemSetting<String>("jwtKeyAlias")

            JwtServiceImpl(URL(jwtKeyStore), jwtKeyPassword, jwtKeyAlias)
        }
    }
}
