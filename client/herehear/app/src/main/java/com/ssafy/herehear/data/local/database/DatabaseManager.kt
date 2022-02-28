package com.ssafy.herehear.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Library

@Database(entities = [Library::class], version = 1)
abstract class DatabaseManager: RoomDatabase() {
    abstract val libraryDao: LibraryDao
}