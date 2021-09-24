package com.ssafy.herehear.model.network.response

data class LoginRequest(
    var userId: String,
    var userPassword: String
)
