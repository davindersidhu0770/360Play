package com.a360play.a360nautica.data.booking

data class GamePassTime(
    val id: Int,
    val gameId: Int,
    val typeId: Int,
    val passId: Int,
    val time: Int,
    val timeDetails: String,
    val persons: Int,
    val personEntryCount: Int,
    val entryFee: Double,
    val bogoAvailable: Boolean,
    val isMonthlyPass: Boolean,
    val price: Double,
    val vatPercentage: Double,
    val vatAmount: Double,
    var isTimeSelected: Boolean,
    val totalPrice: Double

)