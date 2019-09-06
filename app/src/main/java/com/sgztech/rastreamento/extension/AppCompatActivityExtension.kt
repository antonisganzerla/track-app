package com.sgztech.rastreamento.extension

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun showLog(message: String) {
    Log.w("TAG_DEBUG", message)
}

fun AppCompatActivity.openActivity(cls: Class<*>, finish: Boolean = false) {
    val intent = Intent(this, cls)
    startActivity(intent)
    if(finish){
        finish()
    }
}

fun AppCompatActivity.hideKeyBoard(view: View) {
    val inputMethodManager =
        applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}