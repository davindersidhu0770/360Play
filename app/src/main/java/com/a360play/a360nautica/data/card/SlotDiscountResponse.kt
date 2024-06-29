package com.a360play.a360nautica.data.card

data class SlotDiscountResponse(
    val `data`: List<SlotData>,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

data class SlotData(
    val Card: String,
    val Discount: Int,
    val Id: Int,
    val Type: String
)