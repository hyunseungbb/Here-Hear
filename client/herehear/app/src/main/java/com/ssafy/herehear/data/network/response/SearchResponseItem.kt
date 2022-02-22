package com.ssafy.herehear.data.network.response

data class SearchResponseItem (
    val id: Int,
    val img_url: String,
    val title: String,
    val book_id: Long
        )