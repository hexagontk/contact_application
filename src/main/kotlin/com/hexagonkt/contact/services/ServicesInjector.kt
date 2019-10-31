package com.hexagonkt.contact.services

import com.hexagonkt.helpers.Resource
import com.hexagonkt.helpers.require
import com.hexagonkt.injection.InjectionManager
import com.hexagonkt.settings.SettingsManager

fun configureServicesInjection(injectionManager: InjectionManager) {
    injectionManager {
        bind(JwtService::class) {
            val jwtKeyStore = SettingsManager.settings.require("jwtKeyStore").toString()
            val jwtKeyPassword = SettingsManager.settings.require("jwtKeyPassword").toString()
            val jwtKeyAlias = SettingsManager.settings.require("jwtKeyAlias").toString()

            JwtServiceImpl(Resource(jwtKeyStore), jwtKeyPassword, jwtKeyAlias)
        }
    }
}
