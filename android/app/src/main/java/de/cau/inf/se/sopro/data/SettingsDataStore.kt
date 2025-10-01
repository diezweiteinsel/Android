package de.cau.inf.se.sopro.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val URL_KEY = stringPreferencesKey("url")

suspend fun saveUrl(context: Context, url: String) {
    context.dataStore.edit { settings ->
        settings[URL_KEY] = url
    }
}
fun getUrl(context: Context): String{
    return context.dataStore.data.map { preferences ->
        preferences[URL_KEY]
    } as String
}
