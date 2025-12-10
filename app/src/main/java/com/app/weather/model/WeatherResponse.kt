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

data class PointDataResponse(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("geometry")
    val geometry: Geometry,

    @field:SerializedName("properies")
    val properties: Properties
)

data class Properties(
    @field:SerializedName("@id")
    val id: String,
    @field:SerializedName("@type")
    val type: String?,
    @field:SerializedName("cwa")
    val cwa: String?,
    @field:SerializedName("forcastOffice")
    val forecastOffice: String?,
    @field:SerializedName("gridId")
    val gridId: String?,
    @field:SerializedName("gridX")
    val gridX: String?,
    @field:SerializedName("gridY")
    val gridY: String?,
    @field:SerializedName("forecastGridData")
    val forecastGridData: String?, // Url to the grid data
    @field:SerializedName("observationStations")
    val observationStations: String?, // Url to observation station

//comment out relative location - don't need it right now
//    @field:SerializedName("relativeLocation")
//    val relativeLocation: RelativeLocation,

    /* These are all urls to fetch the data */
    @field:SerializedName("forecastZone")
    val forecastZone: String?,
    @field:SerializedName("county")
    val county: String?,
    @field:SerializedName("fireWeatherZone")
    val fireWeatherZone: String?,

    @field:SerializedName("timeZone")
    val timeZone: String?,

    @field:SerializedName("radarStation")
    val radarStation: String?,
)

data class RelativeLocation(
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("geometry")
    val geometry: Geometry
)

data class Geometry(
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("coordinates")
    val coordinates: List<Double>
)
//    "id": "string",
//    "type": "Feature",
//    "properties": {
//    "geometry": "string",
//    "@id": "string",
//    "@type": "wx:Point",
//    "cwa": "AKQ",
//    "forecastOffice": "string",
//    "gridId": "AKQ",
//    "gridX": 0,
//    "gridY": 0,
//    "forecast": "string",
//    "forecastHourly": "string",
//    "forecastGridData": "string",
//    "observationStations": "string",
//    "forecastZone": "string",
//    "county": "string",
//    "fireWeatherZone": "string",
//    "timeZone": "string",
//    "radarStation": "string"
//}
//}

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