package com.a360play.a360nautica.data.booking

data class NationalityListResponse(
    val `data`: List<NationalityDataResponse>,
    val statusCode: Int,
    val isSuccess: Boolean,
    val status: String,
    val message: Any
)