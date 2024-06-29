package com.a360play.a360nautica.model

import com.a360play.a360nautica.data.login.LoginData

data class LoginResponse(
/*
    val `data`: List<LoginData>,
*/
    val `data`: LoginData,
    val isSuccess: Boolean,
    val statusCode: Int,
    val status: String,
    val message: String
)