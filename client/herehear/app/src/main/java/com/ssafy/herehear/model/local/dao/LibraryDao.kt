package com.ssafy.herehear.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.herehear.model.local.entity.Library
import io.reactivex.Flowable

@Dao
interface LibraryDao {
    @Query("SELECT * FROM ${Library.TABLE_NAME}")
    fun getLibraryWithChanges(): Flowable<List<Library>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibrarys(library: List<Library>)

    @Query("SELECT COUNT(*) FROM ${Library.TABLE_NAME}")
    fun count(): Int
}