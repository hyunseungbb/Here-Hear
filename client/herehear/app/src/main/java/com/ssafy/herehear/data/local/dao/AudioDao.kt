package com.ssafy.herehear.data.local.dao

import androidx.room.*
import com.ssafy.herehear.data.local.entity.Audio
import io.reactivex.Flowable

@Dao
interface AudioDao {

    @Query("SELECT * FROM ${Audio.TABLE_NAME} WHERE ${Audio.COLUMN_BOOK_ID} = :bookId")
    fun getAudioWithChanges(bookId: Int): Flowable<List<Audio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudio(audio: Audio)

    @Delete
    fun deleteAudio(audio: Audio)
}