package com.a360play.a360nautica.data.booking

import java.io.File

data class BookRequestData(

    val gameId: Int,
    val name: String,
    val isdCode: String,
    val mobile: String,
    val gender: String,
    val countryId: Int,
    val payType: String,
    val userId: Int,
    val timeId: Int,
    val quantity: Int,
    val discountId: Int,
    val accessories: String,
    val discountComment: String,
    val CompComment: String,
    val discountImage: String,
    val CompImage: String,
    val promocode: String,
    val promocodeId: Int,
    val orderId: Int,
    val isMonthlyPass: Boolean,
    val notes: String,
    val CashAmount: Double,
    val IsComp: Boolean,
    val personEntryCount: Int,
    val prepaidMode: String
   /* val timeIds: String,
    val passIds: String*/

)