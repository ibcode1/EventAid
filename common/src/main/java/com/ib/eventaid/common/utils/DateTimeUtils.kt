package com.ib.eventaid.common.utils

import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeUtils {
    fun parse(dateTimeString: String): LocalDateTime = try {
        LocalDateTime.parse(dateTimeString)
    } catch (e: Exception) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ssZ", Locale.ENGLISH)
        LocalDateTime.parse(dateTimeString, dateFormatter)
    }
}