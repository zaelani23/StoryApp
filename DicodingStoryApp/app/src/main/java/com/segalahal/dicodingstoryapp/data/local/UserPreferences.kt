package com.segalahal.dicodingstoryapp.data.local

import android.content.Context
import com.segalahal.dicodingstoryapp.data.remote.response.LoginResult

class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        return LoginResult(
            preferences.getString(NAME, "").toString(),
            preferences.getString(USER_ID, "").toString(),
            preferences.getString(TOKEN, "").toString()
        )
    }

    fun deleteUser(){
        val editor = preferences.edit()
        editor.putString(NAME, "")
        editor.putString(USER_ID, "")
        editor.putString(TOKEN, "")
        editor.apply()
    }

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USER_ID = "user_id"
        private const val TOKEN = "token"

        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences =
            instance ?: synchronized(this) {
                instance ?: UserPreferences(context)
            }
    }
}