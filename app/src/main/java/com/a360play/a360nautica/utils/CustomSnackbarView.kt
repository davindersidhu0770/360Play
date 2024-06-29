package com.a360play.a360nautica.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.a360play.a360nautica.R
import com.google.android.material.snackbar.ContentViewCallback

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyle: Int = 0
) : LinearLayout(context, attributeSet, defaultStyle), ContentViewCallback {

    init {
        View.inflate(context, R.layout.item_custom_snackbar, this)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        // TODO("Use some animation")
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        // TODO("Use some animation")
    }

}