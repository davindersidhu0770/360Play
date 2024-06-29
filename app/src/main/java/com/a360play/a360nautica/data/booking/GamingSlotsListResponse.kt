package com.a360play.a360nautica.data.booking

data class GamingListResponse(
    val `data`: DataGames,
    val statusCode: Int,
    val status: String,
    val message: String,
    val isSuccess: Boolean
)

data class DataGames(
    val games: List<GamingListData>,
    val gameAccessories: List<GameAccessoriesItem>,
    val countries: List<NationalityDataResponse>,
    val prepaidModes: List<PrepaidModeResponse>

)

