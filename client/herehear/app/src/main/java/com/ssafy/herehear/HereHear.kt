package com.ssafy.herehear

import android.app.Application
import com.ssafy.herehear.model.Preference

class HereHear : Application() {
    companion object {
        lateinit var prefs: Preference
    }

    override fun onCreate() {
        prefs=Preference(applicationContext)
        super.onCreate()
    }
}