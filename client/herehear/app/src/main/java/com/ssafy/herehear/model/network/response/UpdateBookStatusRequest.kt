package com.ssafy.herehear.model.network.response

data class UpdateBookStatusRequest(
    val id: Int,
    val read_status: Int,
    val stars: Int
)