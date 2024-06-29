package com.a360play.a360nautica.data.entrypoint

import com.a360play.a360nautica.data.booking.AccessoryResponse

data class UserEntryResponse(
    val `data`: Data,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any

)

data class Data(
    val message: String,
    val Response: Response,
    val status: Boolean
)

data class Response(
    val active: Boolean,
    val actualDuration: Int,
    val additionalCost: Int,
    val amount: Int,
    val balanceDuration: BalanceDuration,
    val barcode: Any,
    val bookedBy: Int,
    val categoryName: String,
    val countrycode: Any,
    val created: String,
    val customer: String,
    val duration: Int,
    val email: String,
    val endTime: String,
    val entryBy: Int,
    val exitBy: Int,
    val id: Int,
    val ipaddress: String,
    val mobile: String,
    val ServiceType: String,
    val option: Int,
    val rideName: String,
    val payment_Type: String,
    val startTime: String,
    val status: String,
    val Offer: String,
    val ticketcount: Int,
    val VATPercent: Int,
    val VATAmount: Double,
    val AddVATAmount: Double,
    val FinalAmount: Double,
    val updated: String,
    val discountAmount: Double,
    val CDR_Id: Int,
    val CardDetails: String,
    val CardName: String,
    val TotalAmount: Double,
    val ExtraPlayDuration: String,
    val ListAccessories: List<AccessoryResponse>,

    )

data class BalanceDuration(
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