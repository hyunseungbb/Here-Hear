package com.ssafy.herehear.data.local.dao

import androidx.room.*
import com.ssafy.herehear.data.local.entity.Library
import io.reactivex.Flowable

@Dao
interface LibraryDao {
    @Query("SELECT * FROM ${Library.TABLE_NAME}")
    fun getLibraryWithChanges(): Flowable<List<Library>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibrarys(library: List<Library>)

    @Query("SELECT COUNT(*) FROM ${Library.TABLE_NAME}")
    fun count(): Int

    @Query("SELECT * FROM ${Library.TABLE_NAME} WHERE ${Library.COLUMN_BOOK_ID} = :bookId")
    fun getDetailWithChanges(bookId: Int): Flowable<Library>

    @Query("SELECT * FROM ${Library.TABLE_NAME} WHERE ${Library.COLUMN_BOOK_ID} = :bookId")
    fun getDetail(bookId: Int): Library?

    @Delete
    fun deleteLibrary(library: Library)
}