package com.a360play.a360nautica.data.booking

data class CheckResponse(
    val `data`: CheckResponseItem,
    val statusCode: Int,
    val isSuccess: Boolean,
    val status: String,
    val message: String
)

data class CheckResponseItem(

    val forceUpdate: Boolean,
    val appVersion: String

)