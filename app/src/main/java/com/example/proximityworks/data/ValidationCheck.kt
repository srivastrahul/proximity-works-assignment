package com.example.proximityworks.data

import com.example.proximityworks.BaseApplication
import com.example.proximityworks.R

data class ValidationCheck(
    val message: String = BaseApplication.applicationContext()
        .getString(R.string.something_went_wrong),
    var type: Int = 0
)