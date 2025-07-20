package com.app.weather.model

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class WeatherResponse(
    @field:SerializedName("temperature")
    val temperature: String,
    @field:SerializedName("wind")
    val wind: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("forecast")
    val forecast: List<Forecast>
)

fun String.toFahrenheitSafe(): String? {
    return try {
        // Remove common temperature suffixes and extra whitespace
        val cleanedInput = this.trim()
            .replace(Regex("°?[Cc]"), "") // Remove °C or C
            .replace(Regex("[^-\\d.\\s]"), "") // Keep only digits, dots, minus, and spaces
            .trim()

        val celsius = cleanedInput.toDoubleOrNull() ?: return null

        // Check for reasonable temperature range
        if (celsius < -273.15) return null // Below absolute zero

        val fahrenheit = (celsius * 9.0 / 5.0) + 32.0
        "${fahrenheit.roundToInt()} °F"
    } catch (e: Exception) {
        null
    }
}

data class Forecast(
    @field:SerializedName("day")
    val day: String,
    @field:SerializedName("temperature")
    val temperature: String,
    @field:SerializedName("wind")
    val wind: String
)

// Response example
//{
//    "temperature": "+25 °C",
//    "wind": "20 km/h",
//    "description": "Clear",
//    "forecast": [
//    {
//        "day": "1",
//        "temperature": "27 °C",
//        "wind": "22 km/h"
//    },
//    {
//        "day": "2",
//        "temperature": "+26 °C",
//        "wind": "26 km/h"
//    },
//    {
//        "day": "3",
//        "temperature": " °C",
//        "wind": "+27 km/h"
//    }
//    ]
//}