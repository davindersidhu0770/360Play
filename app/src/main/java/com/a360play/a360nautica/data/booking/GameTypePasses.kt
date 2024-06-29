package com.a360play.a360nautica.data.booking

data class GameTypePasses(
    val id: Int,
    val gameId: Int,
    val typeId: Int,
    val pass: String,
    var isSelected: Boolean,
    val gamePassTimes: List<GamePassTime>


)