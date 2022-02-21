package com.ssafy.herehear.model.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Library")
data class Library(
    @PrimaryKey
    val id: String,
    val book_id: Int,
    val img_url: String,
    val read_status: Int,
    val stars: Int
)
