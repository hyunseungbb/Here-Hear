package com.ssafy.herehear

import android.app.Application
import android.content.Context
import com.ssafy.herehear.model.Preference

class HereHear : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var prefs: Preference
        var instance: HereHear? = null
        fun context(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        prefs=Preference(applicationContext)
        super.onCreate()
    }
}