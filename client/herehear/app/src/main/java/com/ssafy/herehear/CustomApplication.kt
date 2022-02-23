package com.ssafy.herehear

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ssafy.herehear.data.Preference
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.database.DatabaseManager
import com.ssafy.herehear.data.repository.LibraryMainRepository
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {

//    private lateinit var databaseManager: DatabaseManager
//
//    lateinit var libraryDao: LibraryDao
//
//    lateinit var uiThreadScheduler: UIThreadScheduler
//        private set
//
//    lateinit var networkThreadScheduler: NetworkThreadScheduler
//        private set
//
//    lateinit var ioThreadScheduler: IoThreadScheduler
//        private set
//
//    lateinit var libraryMainRepository: LibraryMainRepository

    init {
        instance = this
    }

    companion object {
        var stars: Int = 0
        var myBookStatus: Int = 0
        lateinit var prefs: Preference
        var instance: CustomApplication? = null
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

//        databaseManager = Room.databaseBuilder(this, DatabaseManager::class.java, "database")
//            .fallbackToDestructiveMigration() //
//            .allowMainThreadQueries() // TODO: count()
//            .build();
//        libraryDao = databaseManager.libraryDao
//
//        uiThreadScheduler = UIThreadScheduler()
//        ioThreadScheduler = IoThreadScheduler()
//        networkThreadScheduler = NetworkThreadScheduler()
//        libraryMainRepository = LibraryMainRepository(libraryDao, networkThreadScheduler, ioThreadScheduler)
    }

}