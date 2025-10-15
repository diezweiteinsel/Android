package de.cau.inf.se.sopro.di

import android.content.Context
import android.content.SharedPreferences

class UrlManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BASE_URL = "base_url"
        const val DEFAULT_URL = "http://134.245.1.240:1203/"
        //const val DEFAULT_URL = "http://10.0.2.2:8083/"
    }

    fun saveUrl(url: String) {
        prefs.edit().putString(KEY_BASE_URL, url).apply()
    }

    fun getUrl(): String {
        return prefs.getString(KEY_BASE_URL, DEFAULT_URL) ?: DEFAULT_URL
    }
}