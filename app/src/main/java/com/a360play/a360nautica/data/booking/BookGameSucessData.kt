package com.a360play.a360nautica.data.booking

import com.a360play.a360nautica.data.login.LoginData

data class BookGameSucessData(
    val `data`: Data,
    val isSuccess: Boolean,
    val statusCode: Int,
    val status: String,
    val message: String
)

data class Data(
    val id: Long,
    val mapId: Int,
    val quantity: Int,
    val vatPercentage: Int,
    val BOGOApplied: Boolean,
    val price: Double,
    val startTime: String,
    val time: String,
    val persons: Int,
    val personEntryCount: Int,
    val totalPrice: Double,
    val additionalAmount: Double,
    val mobile: String,
    val payType: String,
    val totalAmount: Double,
    val accessoryAmount: Double,
    val passTime: String,
    val name: String,
    val gender: String,
    val isdCode: String,
    val countryId: Int,
    val gameId: Int,
    val typeId: Int,
    val passId: Int,
    val timeId: Int,
    val bookedBy: Int,
    val duration: Int,
    val noOfPax: Int,
    val status: String,
    val bookedOn: String,
    val passEndDate: String,
    val bogoApplied: Boolean,
    val discountDetails: String,
    val discountAmount: Double,
    val totalVATAmount: Double,
    val entryBy: Int,
    val gamesLeft: Int,
    val extraDuration: Int,
    val isMonthlyPass: Boolean,
    val additionalVATAmount: Double,
    val entryFee: Double,
    val vatNo :String,
    val address:String,
    val crNo:String,
    val `accessories`: List<PrintAccessory>

)

data class PrintAccessory(
    val id: Int,
    val accOrderId: Int,
    val accessory: String,
    val accessoryId: Int,
    val price: Double,
    val quantity: Int,
    val totalPrice: Double
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