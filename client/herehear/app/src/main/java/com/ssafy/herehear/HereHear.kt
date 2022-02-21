package com.ssafy.herehear

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ssafy.herehear.model.Preference
import com.ssafy.herehear.model.local.dao.LibraryDao
import com.ssafy.herehear.model.local.database.DatabaseManager
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import com.ssafy.herehear.util.schedulers.UIThreadScheduler

class HereHear : Application() {

    private lateinit var databaseManager: DatabaseManager

    lateinit var libraryDao: LibraryDao

    lateinit var uiThreadScheduler: ThreadScheduler
        private set

    lateinit var networkThreadScheduler: ThreadScheduler
        private set

    lateinit var ioThreadScheduler: ThreadScheduler
        private set


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
        super.onCreate()
        prefs=Preference(applicationContext)

        databaseManager = Room.databaseBuilder(this, DatabaseManager::class.java, "database")
            .fallbackToDestructiveMigration() //
            .allowMainThreadQueries() // TODO: count()
            .build();
        libraryDao = databaseManager.libraryDao

        uiThreadScheduler = UIThreadScheduler()
        ioThreadScheduler = IoThreadScheduler()
        networkThreadScheduler = NetworkThreadScheduler()

    }

}