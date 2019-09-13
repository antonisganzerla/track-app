package com.sgztech.rastreamento.extension

import com.sgztech.rastreamento.util.CodeUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.toDatePtBr(): String {
    val formatPtBrDate = SimpleDateFormat(PATTERN_PT_BR_DATE, LOCALE)
    return try {
        val dateWithTimeZone = SimpleDateFormat(PATTERN_JSON_DATE, LOCALE).parse(this)
        formatPtBrDate.format(dateWithTimeZone)
    } catch (e: ParseException) {
        formatPtBrDate.format(Date())
    }
}

fun String.matchCode(): Boolean{
    return this.matches(Regex(CodeUtil.REGEX_TRACK_CODE))
}

const val PATTERN_PT_BR_DATE = "dd/MM/yyyy HH:mm"
const val PATTERN_JSON_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
val LOCALE: Locale = Locale.ENGLISH