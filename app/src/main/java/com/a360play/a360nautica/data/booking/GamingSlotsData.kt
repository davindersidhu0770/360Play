package com.a360play.a360nautica.data.booking

data class GamingListData(
    val id: Int,
    val country: String,
    val region: String,
    val mall: String,
    val game: String,
    val gameTypes: List<GameTypeResponse>,
//  val gameAccessories: List<GameAccessories>,
    val gameDiscounts: List<GameDiscounts>

)