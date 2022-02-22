package com.ssafy.herehear.data.network.response

data class BookDetailResponse(
    val description: String,
    val id: Int,
    val img_url: String,
    val stars_count: Int,
    val stars_sum: Int,
    val title: String
)