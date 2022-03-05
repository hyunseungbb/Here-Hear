package com.ssafy.herehear

import android.content.Context
import androidx.room.Room
import com.ssafy.herehear.data.local.dao.BookDao
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.database.DatabaseManager
import com.ssafy.herehear.data.repository.LibraryDetailRepository
import com.ssafy.herehear.data.repository.LibraryMainRepository
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiModule {

    @Singleton
    @Provides
    fun provideLibraryMainRepository(
        libraryDao: LibraryDao,
        networkThreadScheduler: NetworkThreadScheduler,
        ioThreadScheduler: IoThreadScheduler
    ) : LibraryMainRepository {
        return LibraryMainRepository(libraryDao, networkThreadScheduler, ioThreadScheduler)
    }

    @Singleton
    @Provides
    fun provideLibraryDetailRepository(
        libraryDao: LibraryDao,
        bookDao: BookDao,
        networkThreadScheduler: NetworkThreadScheduler,
        ioThreadScheduler: IoThreadScheduler
    ) : LibraryDetailRepository {
        return LibraryDetailRepository(libraryDao, bookDao, networkThreadScheduler, ioThreadScheduler)
    }

    @Singleton
    @Provides
    fun provideDatabaseManager(@ApplicationContext context: Context) : DatabaseManager {
        return Room.databaseBuilder(
            context,
            DatabaseManager::class.java,
            "database"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideLibraryDao(databaseManager: DatabaseManager): LibraryDao {
        return databaseManager.libraryDao
    }

    @Singleton
    @Provides
    fun provideBookDao(databaseManager: DatabaseManager): BookDao {
        return databaseManager.bookDao
    }

    @Singleton
    @Provides
    fun provideUiThreadScheduler(): UIThreadScheduler {
        return UIThreadScheduler()
    }

    @Singleton
    @Provides
    fun provideIoThreadScheduler(): IoThreadScheduler {
        return IoThreadScheduler()
    }

    @Singleton
    @Provides
    fun provideNetworkThreadScheduler(): NetworkThreadScheduler {
        return NetworkThreadScheduler()
    }
}

