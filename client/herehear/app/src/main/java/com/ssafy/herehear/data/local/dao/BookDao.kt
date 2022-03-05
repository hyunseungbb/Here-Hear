package com.ssafy.herehear.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.herehear.data.local.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM ${Book.TABLE_NAME} WHERE ${Book.COLUMN_ID} = :bookId")
    fun getDetail(bookId: Int): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: List<Book>)
}