package com.a360play.a360nautica.data.login

import com.a360play.a360nautica.data.booking.NationalityDataResponse

data class LoginData(

    val id: Int,
    val country: String,
    val name: String,
    val currency: String,
    val timeZone: String,
    val book: Boolean,
    val entry: Boolean,
    val exit: Boolean

    /* val active: Boolean,
 val age: Int,
 val countrycode: Any,
 val created: String,
 val dob: String,
 val email: String,
 val gender: Any,
 val groupId: Int,
 val image: Any,
 val ipaddress: String,
 val isverified: Boolean,
 val lastlogin: String,
 val mobile: Any,
 val name: String,
 val nationality: Any,
 val password: Any,
 val passwordhash: Any,
 val passwordsalt: Any,
 val rolecode: Int,
 val rolename: String,

 val unitId: Int,
 val updated: String,*/
)