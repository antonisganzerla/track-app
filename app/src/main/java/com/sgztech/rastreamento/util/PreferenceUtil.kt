package com.sgztech.rastreamento.util

import android.content.Context
import android.preference.PreferenceManager
import com.sgztech.rastreamento.R


object PreferenceUtil {

    fun getUserId(context: Context): String{
        val key = getKey(context)
        val value = sharedPreferences(context).getString(key, DEFAULT_STRING_VALUE)
        return value ?: DEFAULT_STRING_VALUE
    }

    fun setUserId(context: Context, value: String){
        val key = getKey(context)
        val editor = sharedPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getKey(context: Context): String{
        return context.getString(R.string.app_name)
    }

    @JvmStatic
    private fun sharedPreferences(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)

    private const val DEFAULT_STRING_VALUE = ""
}