package com.ssafy.herehear.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssafy.herehear.data.local.entity.Book.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Book(
    @PrimaryKey
    val id: Long,
    val description: String,
    val img_url: String,
    val title: String
) {
    companion object {
        const val TABLE_NAME = "Book"
        const val COLUMN_ID = "id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_IMG_URL = "img_url"
        const val COLUMN_TITLE = "title"
    }
}
