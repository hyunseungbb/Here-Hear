package com.ssafy.herehear.data

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Preference(context: Context) {
    private val preference = "preference"
//    private val ACCESS_TOKEN = "access_token"
    private val prefs=context.getSharedPreferences(preference, MODE_PRIVATE)

    fun getString(key: String?, defValue: String?): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String?, str: String?) {
        prefs.edit().putString(key, str).apply()
    }

//    fun getAccessToken(): String? {
//        return prefs.getString(ACCESS_TOKEN, "")
//    }
//
//    fun setAccessToken(token: String) {
//        setString(ACCESS_TOKEN, token)
//    }

}