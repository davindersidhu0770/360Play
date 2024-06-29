package com.a360play.a360nautica.data.booking

data class GetAgeGroupResponse(
    val `data`: List<AgeGroupData>,
    val status: Int,
    val status_message: String,
    val status_message_arabic: Any
)

data class AgeGroupData(
    val active: Boolean,
    val created: String,
    val id: Int,
    val ipaddress: Any,
    val name: String,
    val updated: String
)