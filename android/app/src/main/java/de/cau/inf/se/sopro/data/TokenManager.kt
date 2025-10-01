package de.cau.inf.se.sopro.data

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val AUTH_TOKEN = "auth_token"
        private const val USER_ID = "user_id"
    }

    fun saveJwt(jwt: String) {
        prefs.edit().putString(AUTH_TOKEN, jwt).commit()
    }

    fun saveUserId(id: Int) {
        prefs.edit().putInt(USER_ID, id).commit()
    }

    fun getUserId(): Int? {
        val id = prefs.getInt(USER_ID, -1)
        return if (id != -1) id else null
    }

    fun getJwt(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun clearJwt() {
        prefs.edit().remove(AUTH_TOKEN).commit()
    }

    fun clearAll() {
        prefs.edit()
            .remove(AUTH_TOKEN)
            .remove(USER_ID)
            .commit()
    }
}