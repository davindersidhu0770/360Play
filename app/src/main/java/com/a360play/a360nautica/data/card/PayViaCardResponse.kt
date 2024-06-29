package com.a360play.a360nautica.data.card

data class PayViaCardResponse(
    val `data`: PayViaData,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

data class PayViaData(
    val message: String,
    val response: PayResponse,
    val status: Boolean
)

data class PayResponse(
    val admin_id: Int,
    val allowed_Email_Ref: Any,
    val amount: Int,
    val balance: Int,
    val barcode: String,
    val coupon_type: Any,
    val currency: Any,
    val email: String,
    val expiry_date: Any,
    val generation_date: String,
    val id: Int,
    val invoice: Any,
    val is_active: Boolean,
    val is_deleted: Boolean,
    val name: String,
    val payment_type: Any,
    val phone: String,
    val prefix: Any,
    val redeemed_status: Int,
    val updated_date: String,
    val user_id: Int
)