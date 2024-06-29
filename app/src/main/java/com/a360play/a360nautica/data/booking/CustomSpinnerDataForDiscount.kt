package com.a360play.a360nautica.data.booking

class CustomSpinnerDataForDiscount (
    var id: Int = 0,
    var name: String = "",
    var value:Int=0
){

    override fun toString(): String {
        return name
    }
}