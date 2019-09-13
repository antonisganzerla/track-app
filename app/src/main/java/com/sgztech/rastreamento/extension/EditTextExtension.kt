package com.sgztech.rastreamento.extension

import android.widget.EditText
import com.sgztech.rastreamento.R

fun EditText.validate(regexValidate: Boolean = false): Boolean {
    val text = this.text.toString()
    if(text.isEmpty()){
        this.error = context.getString(R.string.dialog_et_error)
        return false
    }

    if(regexValidate && !text.matchCode()){
        this.error = context.getString(R.string.msg_enter_valid_code)
        return false
    }
    return true
}