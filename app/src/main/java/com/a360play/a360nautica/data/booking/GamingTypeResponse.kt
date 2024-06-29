package com.a360play.a360nautica.data.booking

data class GameTypeResponse(
    val id: Int,
    val gameId: Int,
    val isMultiSelect: Boolean,
    val type: String,
    val gameTypePasses: List<GameTypePasses>


)