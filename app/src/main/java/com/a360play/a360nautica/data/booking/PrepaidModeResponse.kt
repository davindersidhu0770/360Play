package com.a360play.a360nautica.data.booking

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class PrepaidModeResponse(
    var id: Int,
    var mode: String

    ){


    override fun toString(): String {
        return mode
    }
}
