package com.ssafy.herehear.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssafy.herehear.data.local.entity.Audio.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Audio(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val book_id: Long,
    val directory: String
) {
    companion object {
        const val TABLE_NAME = "Audio"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BOOK_ID = "book_id"
        const val COLUMN_DIRECTORY = "directory"
    }
}
