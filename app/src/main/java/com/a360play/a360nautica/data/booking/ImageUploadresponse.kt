package com.a360play.a360nautica.data.booking

import com.a360play.a360nautica.data.login.LoginData

data class ImageUploadresponse(
    val `data`: DataResponse,
    val isSuccess: Boolean,
    val statusCode: Int,
    val status: String,
    val message: String
)

data class DataResponse(
    val uniqueFileName: String,
    val uniqueCompName: String

)

