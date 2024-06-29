package com.a360play.a360nautica.data.booking

data class EnquiryRequestData(
    val Option: String,
    val SubOption: String,
    val Name: String,
    val Mobile: String,
    val EventDate: String,
    val NoOfKids: Int,
    val NoOfAdults: Int,
    val TimeSlot: String

)