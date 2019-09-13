package com.sgztech.rastreamento.extension

import com.sgztech.rastreamento.util.CodeUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.toDate(): Date {
    val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
    return try {
        val date = this.substring(0, 10).plus(" ").plus(this.substring(11, 19))
        dateFormat.parse(date)
    } catch (e: ParseException) {
        Date()
    }
}

fun String.matchCode(): Boolean{
    return this.matches(Regex(CodeUtil.REGEX_TRACK_CODE))
}