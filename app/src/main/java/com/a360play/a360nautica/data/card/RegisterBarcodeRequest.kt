package com.a360play.a360nautica.data.card

class RegisterBarcodeRequest (
    val adminid:Int,
    val barcode:String,
    val name:String,
    val email : String,
    val phone : String,
    val amount:Int,
    val payment_type:String
)