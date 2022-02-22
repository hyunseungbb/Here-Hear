package com.ssafy.herehear.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssafy.herehear.data.local.entity.Library.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Library(
    @PrimaryKey
    val book_id: Int,
    val id: Int,
    val img_url: String,
    val read_status: Int,
    val stars: Int
) {
    companion object {
        const val TABLE_NAME = "Library"
        const val COLUMN_LIBRARY_ID = "id"
        const val COLUMN_BOOK_ID = "book_id"
        const val COLUMN_IMG_URL = "img_url"
        const val COLUMN_READ_STATUS = "read_status"
        const val COLUMN_START = "stars"
    }
}
