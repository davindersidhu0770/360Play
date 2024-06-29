package com.a360play.a360nautica.data.booking

data class GameAccessoriesItem(
    val id: Long,
    val accessory: String,
    val listGameAccessory: List<ListGameAccessoryItem>

    /*val mapId: Int,
    var quantity: Int,
    var selectedQuantity: Int=0,
    val accessory: String,
    val option: String,
    val optionDetails: String,
    val subOption: String,
    val subOptionDetails: String,
    val price: Double*/

)