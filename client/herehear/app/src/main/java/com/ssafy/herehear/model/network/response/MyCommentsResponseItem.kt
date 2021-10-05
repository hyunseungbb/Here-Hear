package com.ssafy.herehear.model.network.response

data class MyCommentsResponseItem(
    val book: Book,
    val content: String,
    val date: String,
    val id: Int,
    val isshow: Boolean,
    val reading_time: Int,
    val username: String
)