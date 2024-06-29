package com.a360play.a360nautica.data.booking

data class NameSuggestionsResponse(
    val data: List<Suggestions>,
    val statusCode: Int,
    val status: String,
    val message: String,
    val isSuccess: Boolean
)
data class Suggestions(
    val name: String

    )

