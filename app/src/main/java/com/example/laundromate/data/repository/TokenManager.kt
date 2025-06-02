package com.example.laundromate.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val accessTokenKey = stringPreferencesKey("access_token")

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[accessTokenKey] }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { it[accessTokenKey] = token }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}