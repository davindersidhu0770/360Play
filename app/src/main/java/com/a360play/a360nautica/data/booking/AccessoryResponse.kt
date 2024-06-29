package com.a360play.a360nautica.data.booking

import java.io.Serializable

class AccessoryResponse (
    val Id: Int,
    val AccOrderId: Int,
    val Accessory: String,
    val AccessoryId: Int,
    val Price: Int,
    val Quantity: Int,
    val TotalPrice: Int,
    var isSelected :Boolean = false


): Serializable

