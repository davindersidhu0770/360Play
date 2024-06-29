package com.a360play.a360nautica.data.booking

data class EnquiryGameSucessData(
    val `data`: Data1,
    val status: Int,
    val status_message: String,
    val status_message_arabic: String
)

data class Data1(
    val Message: String,
    val Response: Int,
    val Status: Boolean
)