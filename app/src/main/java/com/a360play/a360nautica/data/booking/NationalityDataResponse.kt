package com.a360play.a360nautica.data.booking

import android.os.Parcel
import android.os.Parcelable

class NationalityDataResponse(
    val id: Int,
    var countryName: String,
    val nationality: String,
    val countryCode: String,
    val isoCode: String,
    val currencyCode: String,
    val isdCode: String,
    val timeZone: String

) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toString(): String {
        return "$nationality"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(countryName)
        parcel.writeString(nationality)
        parcel.writeString(countryCode)
        parcel.writeString(isoCode)
        parcel.writeString(currencyCode)
        parcel.writeString(isdCode)
        parcel.writeString(timeZone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NationalityDataResponse> {
        override fun createFromParcel(parcel: Parcel): NationalityDataResponse {
            return NationalityDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<NationalityDataResponse?> {
            return arrayOfNulls(size)
        }
    }

}

