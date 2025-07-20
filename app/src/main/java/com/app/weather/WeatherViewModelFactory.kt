package com.app.weather

import WeatherService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(
    private val weatherService: WeatherService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherService) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}