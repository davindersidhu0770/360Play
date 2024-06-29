package com.a360play.a360nautica.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snack(message :String , action:  String ){
    Snackbar.make(this, message , Snackbar.LENGTH_SHORT).setAction(action) { }
    .show()

}