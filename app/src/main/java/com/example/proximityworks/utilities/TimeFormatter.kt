package com.example.proximityworks.utilities

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    fun convertTimestampTo12HourFormat(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return timeFormat.format(timestamp)
    }
}