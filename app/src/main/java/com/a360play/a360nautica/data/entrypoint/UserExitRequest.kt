package com.a360play.a360nautica.data.entrypoint

class UserExitRequest (
    val id: Int,
    val userId: Int,
    val extraDuration: Int,
    val additionalAmount:Double,
    val additionalVATAmount:Double,
    val CashAmount:Double,
    val payType:String,
    val additionalEntryFee:Double
    )