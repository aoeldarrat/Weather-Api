package com.app.weather.model

import com.google.gson.annotations.SerializedName

data class StationResponse(
    @field:SerializedName("@id")
    val id: String,

    @field:SerializedName("@features")
    val features: List<StationFeature>
)

data class StationFeature(
    @field:SerializedName("@id")
    val id: String,

    @field:SerializedName("@type")
    val type: String,

    @field:SerializedName("@properties")
    val properties: StationProperties
)

data class StationProperties(
    @field:SerializedName("@geometry")
    val geometry: Geometry,
    @field:SerializedName("@id")
    val id: String,
    @field:SerializedName("@type")
    val type: String,
    @field:SerializedName("@elevation")
    val elevation: BaseMeasurement,
    @field:SerializedName("@stationIdentifier")
    val stationIdentifier: String,
    @field:SerializedName("@name")
    val name: String,
    @field:SerializedName("@timeZone")
    val timeZone: String,
    @field:SerializedName("@provider")
    val provider: String,
    @field:SerializedName("@subProvider")
    val subProvider: String,
    @field:SerializedName("@forecast")
    val forecast: String,
    @field:SerializedName("@county")
    val county: String,
    @field:SerializedName("@fireWeatherZone")
    val fireWeatherZone: String,
    @field:SerializedName("@distance")
    val distance: BaseMeasurement,
)

data class BaseMeasurement(
    @field:SerializedName("@value")
    val value: Int,
    @field:SerializedName("@maxValue")
    val maxValue: Int,
    @field:SerializedName("@minValue")
    val minValue: Int,
    @field:SerializedName("@unitCode")
    val unitCode: String,
    @field:SerializedName("@qualityControl")
    val qualityControl: String
)

/*
{
  "type": "FeatureCollection",
  "features": [
    {
      "id": "string",
      "type": "Feature",
      "properties": {
        "geometry": "string",
        "@id": "string",
        "@type": "wx:ObservationStation",
        "elevation": {
          "value": 0,
          "maxValue": 0,
          "minValue": 0,
          "unitCode": "string",
          "qualityControl": "Z"
        },
        "stationIdentifier": "string",
        "name": "string",
        "timeZone": "string",
        "provider": "string",
        "subProvider": "string",
        "forecast": "string",
        "county": "string",
        "fireWeatherZone": "string",
        "distance": {
          "value": 0,
          "maxValue": 0,
          "minValue": 0,
          "unitCode": "string",
          "qualityControl": "Z"
        },
        "bearing": {
          "value": 0,
          "maxValue": 0,
          "minValue": 0,
          "unitCode": "string",
          "qualityControl": "Z"
        }
      }
    }
  ],
  "observationStations": [
    "string"
  ],
  "pagination": {
    "next": "string"
  }
}
 */