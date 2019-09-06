package com.sgztech.rastreamento.extension

import android.widget.EditText
import com.sgztech.rastreamento.R

fun EditText.validate(): Boolean {
    if(this.text.isEmpty()){
        this.error = context.getString(R.string.dialog_et_error)
        return false
    }
    return true
}