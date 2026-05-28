package com.burelo.appmedicinal.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesRepository(private val context: Context) {

    companion object {
        private val FAVORITES_KEY = stringPreferencesKey("favorite_names")
    }

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[FAVORITES_KEY]
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?.toSet()
            ?: emptySet()
    }

    suspend fun toggle(name: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY]
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
            val updated = if (name in current) current - name else current + name
            prefs[FAVORITES_KEY] = updated.joinToString(",")
        }
    }

    suspend fun isFavorite(name: String): Boolean {
        var result = false
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY]
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
            result = name in current
        }
        return result
    }
}
