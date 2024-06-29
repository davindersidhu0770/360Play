package com.a360play.a360nautica.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.a360play.a360nautica.R
import com.google.android.material.snackbar.BaseTransientBottomBar


class CustomSnackbar(
    parent: ViewGroup,
    content: CustomSnackbarView
) : BaseTransientBottomBar<CustomSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(viewGroup: ViewGroup,message:String): CustomSnackbar {
            val customView = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.layout_custom_snackbar,
                viewGroup,
                false
            ) as CustomSnackbarView

            customView.findViewById<TextView>(R.id.tvMessage).setText(message)

            return CustomSnackbar(viewGroup, customView)
        }
        
    }

}