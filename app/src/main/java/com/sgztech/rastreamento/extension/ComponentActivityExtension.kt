package com.sgztech.rastreamento.extension

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity

fun showLog(message: String) {
    Log.w("TAG_DEBUG", message)
}

fun ComponentActivity.openActivity(cls: Class<*>, finish: Boolean = false) {
    startActivity(getIntent(cls))
    if(finish){
        finish()
    }
}

fun ComponentActivity.getIntent(cls: Class<*>): Intent{
    return Intent(this, cls)
}


fun ComponentActivity.hideKeyBoard(view: View) {
    val inputMethodManager =
        applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}