package com.app.weather.navigation

import kotlinx.serialization.Serializable
// Landing page
@Serializable
object Home

// Weather page (deprecated?)
@Serializable
data class Weather(val state: String)

// Settings page
@Serializable
object Settings

// Interactive map
@Serializable
object MapPage

