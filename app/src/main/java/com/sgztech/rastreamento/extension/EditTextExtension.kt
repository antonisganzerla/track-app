package com.sgztech.rastreamento.extension

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.rastreamento.R

fun EditText.validate(regexValidate: Boolean = false, textInputLayout: TextInputLayout): Boolean {
    textInputLayout.isErrorEnabled = true
    val text = this.text.toString()
    if(text.isEmpty()){
        textInputLayout.error = context.getString(R.string.dialog_et_error)
        return false
    }

    if(regexValidate && !text.matchCode()){
        textInputLayout.error = context.getString(R.string.msg_enter_valid_code)
        return false
    }
    textInputLayout.isErrorEnabled = false
    return true
}