package com.ssafy.herehear.model.network.response

data class SearchResponseItem (
    val id: Int,
    val img_url: String,
    val title: String,
    val book_id: Long
        )