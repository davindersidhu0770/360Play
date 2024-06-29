package com.a360play.a360nautica.data.booking

import com.a360play.a360nautica.data.login.LoginData

data class PreviewSucessData(
    val `data`: DataPreview,
    val isSuccess: Boolean,
    val statusCode: Int,
    val status: String,
    val message: String
)

data class DataPreview(

    val noOfPax: String,
    val persons: Int,
    val personEntryCount: Int,
    val gamesLeft: Int,
    val promocodeId: Int,
    val discount: String,
    val basePrice: Double,
    val totalAmount: Double,
    val discountAmount: Double,
    val totalVATAmount: Double,
    val totalAccessoryAmount: Double,
    val isMonthlyPass: Boolean

)
