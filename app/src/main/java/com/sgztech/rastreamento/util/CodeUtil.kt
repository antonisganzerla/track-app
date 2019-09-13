package com.sgztech.rastreamento.util

import android.widget.EditText
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.extension.matchCode

object CodeUtil {

    /**
     * builder with only positive callback
     */
    fun isValid(editText: EditText): Boolean{
        val code = editText.text.toString()
        if (code.isEmpty()) {
            SnackBarUtil.showShort(editText, R.string.msg_enter_code)
            return false
        }
        if(!code.matchCode()){
            SnackBarUtil.showShort(editText, R.string.msg_enter_valid_code)
            return false
        }
        return true
    }

    const val REGEX_TRACK_CODE = "^([A-Z]{2}[0-9]{9}[A-Z]{2})\$"
}