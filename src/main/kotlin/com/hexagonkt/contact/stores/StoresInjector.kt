package com.hexagonkt.contact.stores

import com.hexagonkt.contact.stores.impl.ContactStoreImpl
import com.hexagonkt.contact.stores.impl.UserStoreImpl
import com.hexagonkt.injection.Module

fun configureStoresInjection(injectionManager: Module) {
    injectionManager.bind(UserStore::class) { UserStoreImpl() }
    injectionManager.bind(ContactStore::class) { ContactStoreImpl() }
}
