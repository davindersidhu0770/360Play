package com.a360play.a360nautica.data.booking

data class PreviewGameSucessData(
    val `data`: Int,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

//data class Data(
//    val message: String,
//    val Response: Response,
//    val status: Boolean
//)
//
//data class Response(
//    val id: Int,
//    val AccessoriesTotalAmount: Double,
//    val customer: String,
//    val email: String,
//    val active: Boolean,
//    val actualDuration: Int,
//    val additionalCost: Int,
//    val amount: Double,
//    val balanceDuration: BalanceDuration,
//    val ListAccessories: List<AccessoryResponse>,
//    val barcode: Any,
//    val bookedBy: Int,
//    val categoryName: String,
//    val countrycode: Any,
//    val created: String,
//    val duration: Int,
//
//    val endTime: String,
//    val entryBy: Int,
//    val exitBy: Int,
//
//    val ipaddress: String,
//    val mobile: String,
//    val option: Int,
//    val rideName: String,
//    val startTime: String,
//    val status: String,
//    val ticketcount: Int,
//    val payment_Type: String,
//    val updated: String,
//    val discountAmount: Double,
//    val CDR_Id: Int,
//    val CardDetails: String,
//    val CardName: String,
//    val TotalAmount: Double,
//    val ExtraPlayDuration: String
//)
//
//data class BalanceDuration(
//    val days: Int,
//    val hours: Int,
//    val milliseconds: Int,
//    val minutes: Int,
//    val seconds: Int,
//    val ticks: Int,
//    val totalDays: Int,
//    val totalHours: Int,
//    val totalMilliseconds: Int,
//    val totalMinutes: Int,
//    val totalSeconds: Int
//)