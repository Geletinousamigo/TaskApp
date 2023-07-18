package com.nikhil.task.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun updateUserCredentials(userData: UserCredentials) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = userData.username
            preferences[PreferencesKeys.PASSWORD] = userData.password
        }
    }

    val userPreferencesFlow: Flow<UserCredentials> = dataStore.data.map { preferences ->
        UserCredentials(
            username = preferences[PreferencesKeys.USERNAME] ?: "",
            password = preferences[PreferencesKeys.PASSWORD] ?: "",
        )
    }
}


data class UserCredentials(
    val username: String,
    val password: String,
)

private object PreferencesKeys {
    val USERNAME = stringPreferencesKey("username")
    val PASSWORD = stringPreferencesKey("password")
}