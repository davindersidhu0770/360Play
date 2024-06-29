package com.a360play.a360nautica.data.booking

class CustomSpinnerData (
    var id: Int = 0,
    var name: String = ""
){

    override fun toString(): String {
        return name
    }
}