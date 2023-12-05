package com.example.mystamp.utils
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "login_prefs")
class AutoLogin(private val context: Context) {


    private val dataStore = context.dataStore

    private object PreferenceKeys {
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
    }

    suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LOGIN_CHECK] = isLoggedIn
        }
    }
    suspend fun getLoginStatus(): Boolean {
        return dataStore.data.first()[PreferenceKeys.LOGIN_CHECK] ?: false
    }
    suspend fun savePhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.PHONE_NUMBER] = phoneNumber
        }
    }
    suspend fun getPhoneNumber(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.PHONE_NUMBER]
        }.first()
    }
}