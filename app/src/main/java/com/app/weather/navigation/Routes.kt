package com.app.weather.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Weather(val state: String)

@Serializable
object Settings

@Serializable
object MapPage

