package com.sgztech.rastreamento.util

import android.content.Context
import android.text.InputFilter
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.extension.matchCode

object CodeUtil {

    /**
     * builder with only positive callback
     */
    fun isValid(editText: EditText, textInputLayout: TextInputLayout): Boolean{
        val code = editText.text.toString()
        textInputLayout.isErrorEnabled = true
        if (code.isEmpty()) {
            val msg = editText.context.getString(R.string.msg_enter_code)
            SnackBarUtil.showShort(editText, msg)
            textInputLayout.error = msg
            return false
        }
        if(!code.matchCode()){
            val msg = editText.context.getString(R.string.msg_enter_valid_code)
            SnackBarUtil.showShort(editText, R.string.msg_enter_valid_code)
            textInputLayout.error = msg
            return false
        }
        textInputLayout.error = ""
        textInputLayout.isErrorEnabled = false
        return true
    }

    fun filter(context: Context): Array<InputFilter> {
        val maxLength = context.resources.getInteger(R.integer.max_length_code)
        return arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(maxLength))
    }

    const val REGEX_TRACK_CODE = "^([A-Z]{2}[0-9]{9}[A-Z]{2})\$"
}