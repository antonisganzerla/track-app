package com.sgztech.rastreamento.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil{

    fun show(view: View, @StringRes resourceMessage: Int){
        Snackbar.make(view, resourceMessage, Snackbar.LENGTH_LONG).show()
    }


    fun show(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    @JvmStatic
    fun showShort(view: View, @StringRes resourceMessage: Int){
        Snackbar.make(view, resourceMessage, Snackbar.LENGTH_SHORT).show()
    }
}