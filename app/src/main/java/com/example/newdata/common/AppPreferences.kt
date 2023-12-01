package com.example.newdata.common

import android.content.SharedPreferences
import com.google.gson.Gson

class AppPreferences(private val preferences: SharedPreferences, gson: Gson) {
    var userName: String?
        get() = preferences.getString(PREF_USER_NAME, "")
        set(name) {
            preferences.edit().putString(PREF_USER_NAME, name).apply()
        }

    var userId: String?
        get() = preferences.getString(PREF_USER_ID, "")
        set(type) {
            preferences.edit().putString(PREF_USER_ID, type).apply()
        }

    var isUserAdded: Boolean?
        get() = preferences.getBoolean(PREF_IS_USER_ADDED, false)
        set(boolean) {
            if (boolean != null) {
                preferences.edit().putBoolean(PREF_IS_USER_ADDED, boolean).apply()
            }
        }

    companion object {
        var PREF_USER_NAME = "pref.user.name"
        var PREF_USER_ID = "pref.user.id"
        var PREF_IS_USER_ADDED = "pref.is.user.added"
    }
}