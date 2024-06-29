//package com.a360play.a360nautica.view.fragment.book
//
//import com.google.gson.annotations.Expose
//import com.google.gson.annotations.SerializedName
//
//data class GameListResponse(
//    @SerializedName("typeOfActivities")  var typeOfActivities:  ArrayList<TypeOfActivity>? = null,
//)
//
//class TypeOfActivity {
//
//    @SerializedName("activity")  var activity: String? = null
//    @SerializedName("passes")  var passesList: ArrayList<Passes>? = null
//
//}
//
//class Passes {
//    @SerializedName("pass")  var pass: String? = null
//    @SerializedName("typeOfGames")  var typedOfgameList: ArrayList<TypeOfgame>? = null
//
//}
//
//class TypeOfgame {
//    @SerializedName("game")  var game: String? = null
//    @SerializedName("times")  var timesArraylist: ArrayList<TimesArrayList>? = null
//
//}
//
//class TimesArrayList {
//
//    @SerializedName("time")  var time: String? = null
//    @SerializedName("basePrice")  var basePrice: Double? = null
//    @SerializedName("fivePerVATAmount")  var vatAmount: Double? = null
//    @SerializedName("totalAmount")  var totalAmount: Double? = null
//    @SerializedName("person")  var person: Int? = null
//
//}
