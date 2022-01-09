package com.example.proximityworks.data

data class CityAqiTime(
    val city: String? = "",
    val aqi: Double? = 0.0,
    val timestamp: Long? = 0L
)