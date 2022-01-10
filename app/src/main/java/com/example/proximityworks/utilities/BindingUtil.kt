package com.example.proximityworks.utilities

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.proximityworks.BaseApplication
import com.example.proximityworks.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bindAqiToTwoDecimalPlaces")
fun bindAqiToTwoDecimalPlaces(view: TextView, aqi: Double) {
    val aqiString = DecimalFormat("#.##").format(aqi)
    view.text = "AQI: $aqiString"
}

@BindingAdapter("bindLastUpdated")
fun bindLastUpdated(view: TextView, timestamp: Long) {
    val currentTimestamp = Calendar.getInstance().timeInMillis
    val timeDifference = currentTimestamp - timestamp
    val text = "Last Updated: "
    when {
        timeDifference < 60000L -> view.text = text + "A few seconds ago."
        timeDifference in 60000L..119999L -> view.text = text + "A minute ago."
        else -> {
            view.text = text + TimeFormatter.convertTimestampTo12HourFormat(timestamp)
        }
    }
}

@BindingAdapter("bindBackgroundColor")
fun bindBackgroundColor(view: ConstraintLayout, aqi: Double) {
    if (aqi <= 50.0f)
        view.setBackgroundColor(view.context.getColor(R.color.deep_green))
    else if (aqi > 50.0f && aqi <= 100.0f)
        view.setBackgroundColor(view.context.getColor(R.color.bright_green))
    else if (aqi > 100.0f && aqi <= 200.0f)
        view.setBackgroundColor(view.context.getColor(R.color.yellow))
    else if (aqi > 200.0f && aqi <= 300.0f)
        view.setBackgroundColor(view.context.getColor(R.color.orange))
    else if (aqi > 300.0f && aqi <= 400.0f)
        view.setBackgroundColor(view.context.getColor(R.color.bright_red))
    else
        view.setBackgroundColor(view.context.getColor(R.color.deep_red))
}