package com.a360play.a360nautica.data.booking

data class PaymentTypeResponse(
    val `data`: List<PaymentTypeData>,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

data class PaymentTypeData(
    val active: Boolean,
    val created: String,
    val id: Int,
    val ipaddress: Any,
    val name: String,
    val updated: String
)