package com.a360play.a360nautica.data.booking

data class ListGameAccessoryItem(
    val mapId: Int,
    var quantity: Int,
    var selectedQuantity: Int=0,
    val accessory: String,
    val option: String,
    val optionDetails: String,
    val subOption: String,
    val subOptionDetails: String,
    val colorCode: String,
    val price: Double

)