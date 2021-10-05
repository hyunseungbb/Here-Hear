package com.ssafy.herehear.model.network.response

data class GetMyLibraryResponseItem(
    val book_id: Int,
    val id: Int,
    val img_url: String,
    val read_status: Int,
    val stars: Int
)