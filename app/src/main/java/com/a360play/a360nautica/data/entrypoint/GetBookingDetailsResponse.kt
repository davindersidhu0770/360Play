package com.a360play.a360nautica.data.entrypoint

data class GetBookingDetailsResponse(
    val `data`: BookingDetailsData,
    val status: String,
    val message: String,
    val isSuccess: Boolean,
    val statusCode: Int
)

data class BookingDetailsData(
    val id: Int,
    val quantity: Int,
    val name: String,
    val mobile: String,
    val price: Double,
    val payType: String,
    val passTime: String,
    val duration: Int,
    val entryOn: String,
    val beginTime: String,
    val exitOn: String,
    val extraDuration: Int,
    val vatPercentage: Int,
    val totalPrice: Double,
    val totalAmount: Double,
    val discountAmount: Double,
    val totalVATAmount: Double,
    val accessoryAmount: Double,
    val additionalAmount: Double,
    val additionalVATAmount: Double,
    val additionalEntryFee: Double,
    val discountDetails: String,


)

data class BalanceDurationData(
    val days: Int,
    val hours: Int,
    val milliseconds: Int,
    val minutes: Int,
    val seconds: Int,
    val ticks: Int,
    val totalDays: Int,
    val totalHours: Int,
    val totalMilliseconds: Int,
    val totalMinutes: Int,
    val totalSeconds: Int
)