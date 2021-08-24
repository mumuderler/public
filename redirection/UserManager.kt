package com.example.forwarding

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {

    // Create the dataStore and give it a name same as shared preferences
    private val dataStore = context.createDataStore(name = "user_prefs")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val USER_PASSWORD_KEY = preferencesKey<String>("USER_PASSWORD")
    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeUser(password: String) {
        dataStore.edit {
            it[USER_PASSWORD_KEY] = password

            // here it refers to the preferences we are editing

        }
    }

    // Create an age flow to retrieve age from the preferences
    // flow comes from the kotlin coroutine
    val userPasswordFlow: Flow<String> = dataStore.data.map {
        it[USER_PASSWORD_KEY] ?: ""
    }

}
