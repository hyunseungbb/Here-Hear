package com.ssafy.herehear.model.network.response

data class LoginResponse(
    val auth_token: String,
    val message: String,
    val status: Boolean,
    val user: User
)