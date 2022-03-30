package com.ssafy.herehear.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssafy.herehear.data.local.dao.AudioDao
import com.ssafy.herehear.data.local.dao.BookDao
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.data.local.entity.Book
import com.ssafy.herehear.data.local.entity.Library

@Database(entities = [Library::class, Book::class, Audio::class], version = 1)
abstract class DatabaseManager: RoomDatabase() {
    abstract val libraryDao: LibraryDao
    abstract val bookDao: BookDao
    abstract val audioDao: AudioDao
}