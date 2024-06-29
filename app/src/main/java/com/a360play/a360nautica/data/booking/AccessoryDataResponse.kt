package com.a360play.a360nautica.data.booking

import java.io.Serializable

class AccessoryDataResponse (
    val Id: Int,
    val Name: String,
    val Price: Int,
    val CountryId: Int,
    var itemcount: Int = 0,
    var isSelected :Boolean = false

): Serializable

