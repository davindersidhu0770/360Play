package com.a360play.a360nautica.data.card

data class RegisterBarcodeResponse(
    val `data`: RegisterData,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

data class RegisterData(
    val Message: String,
    val Response: RegisterResponse,
    val Status: Boolean
)

data class RegisterResponse(

    val id: Int,
    val barcode: String,
    val amount: Int,
    val currency: Any,
    val balance: Int,
    val name: String,
    val email: String,
    val prefix: Any,
    val phone: String,
    val user_id: Int,
    val redeemed_status: Int,
    val generation_date: String,
    val expiry_date: Any,
    val updated_date: String,
    val coupon_type: Any,
    val invoice: Any,
    val admin_id: Int,
    val allowed_Email_Ref: Any,
    val is_active: Boolean,
    val is_deleted: Boolean,
)