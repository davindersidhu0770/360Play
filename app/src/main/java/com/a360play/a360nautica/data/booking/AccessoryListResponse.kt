package com.a360play.a360nautica.data.booking

data class AccessoryListResponse(
    val `data`: List<AccessoryDataResponse>,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)