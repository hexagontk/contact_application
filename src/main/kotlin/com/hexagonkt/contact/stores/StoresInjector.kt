package com.hexagonkt.contact.stores

import com.hexagonkt.contact.stores.impl.ContactStoreImpl
import com.hexagonkt.contact.stores.impl.UserStoreImpl
import com.hexagonkt.injection.InjectionManager

fun configureStoresInjection(injectionManager: InjectionManager) {
    injectionManager {
        bind(UserStore::class) { UserStoreImpl() }
        bind(ContactStore::class) { ContactStoreImpl() }
    }
}
