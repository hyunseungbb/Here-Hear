package com.ssafy.herehear.model.network.response

data class User(
    val userAddress: String,
    val userBirth: String,
    val userEmergency: String,
    val userGender: String,
    val userId: String,
    val userName: String,
    val userNo: Int,
    val userPassword: String,
    val userPhone: String
)