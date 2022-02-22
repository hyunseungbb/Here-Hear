package com.ssafy.herehear.data.network.response

data class CreateCommentRequest(
    val content: String,
    val isshow: Boolean,
    val reading_time: Int
)