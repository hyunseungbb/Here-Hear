package com.ssafy.herehear
//
//import android.content.Context
//import androidx.room.Room
//import com.ssafy.herehear.data.local.dao.LibraryDao
//import com.ssafy.herehear.data.local.database.DatabaseManager
//import com.ssafy.herehear.data.repository.LibraryMainRepository
//import com.ssafy.herehear.util.schedulers.ThreadScheduler
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class DiModule {
//
//    @Singleton
//    @Provides
//    fun provideLibraryMainRepository(
//        libraryDao: LibraryDao,
//        networkThreadScheduler: ThreadScheduler,
//        ioThreadScheduler: ThreadScheduler
//    ) : LibraryMainRepository {
//        return LibraryMainRepository(libraryDao, networkThreadScheduler, ioThreadScheduler)
//    }
//
//    @Singleton
//    @Provides
//    fun provideDatabaseManager(@ApplicationContext context: Context) : DatabaseManager {
//        return Room.databaseBuilder(
//            context,
//            DatabaseManager::class.java,
//            "database"
//        ).build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideLibraryDao(databaseManager: DatabaseManager): LibraryDao {
//        return databaseManager.libraryDao
//    }
//
//}