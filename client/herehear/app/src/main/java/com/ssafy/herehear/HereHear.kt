package com.ssafy.herehear

import android.app.Application
import android.content.Context
import com.ssafy.herehear.model.Preference

class HereHear : Application() {


    init {
        instance = this
    }

    companion object {
        var stars: Int = 0
        var myBookStatus: Int = 0
        lateinit var prefs: Preference
        var instance: HereHear? = null
        fun context(): Context {
            return instance!!.applicationContext
        }
        fun getBookStars(): Int {
            return stars
        }

        fun setBookStars(stars: Int) {
            this.stars = stars
        }
        fun getBookStatus(): Int {
            return myBookStatus
        }

        fun setBookStatus(myBookStatus: Int) {
            this.myBookStatus = myBookStatus
        }
    }

    override fun onCreate() {
        prefs=Preference(applicationContext)
        super.onCreate()
    }

}