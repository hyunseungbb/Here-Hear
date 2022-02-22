package com.ssafy.herehear.data.network.response

data class LoginResponse(
    val accessToken: String,
    val message: String,
    val refreshToken: String,
    val statusCode: Int,
    val username: String
)