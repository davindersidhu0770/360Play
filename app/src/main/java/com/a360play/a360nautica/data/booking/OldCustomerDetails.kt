package com.a360play.a360nautica.data.booking

data class OldCustomerDetails(
    val `data`: CustromerData,
    val isSuccess: Boolean,
    val message: String,
    val status: String,
    val statusCode: Int
)

data class CustromerData(
    val countryId: Int,
    val typeId: Int,
    val passId: Int,
    val timeId: Int,
    val noOfPax: Int,
    val noOfPerson: Int,
    val discountDetails: String,
    val discountComment: String,
    val gender: String,
    val mobile: String,
    val name: String,
    val notes: String,
    val orderId: String,
    val discountPercentage: Int,
    val payType: String,
    val passFromDate: String,
    val passToDate: String,
    val isMonthlyPass: Boolean
)