package com.app.weather

import WeatherService
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.weather.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherService: WeatherService
) : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentCity = MutableStateFlow("London")
    val currentCity: StateFlow<String> = _currentCity.asStateFlow()

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _currentCity.value = city

                val response = weatherService.getWeather(city)
                _weatherData.value = response

                Log.d("WeatherViewModel", "Weather data loaded successfully for $city")
            } catch (e: Exception) {
                val errorMessage = "Failed to load weather data: ${e.message}"
                _error.value = errorMessage
                _weatherData.value = null

                Log.e("WeatherViewModel", "Error fetching weather data for $city", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchWeather() {
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            getWeatherData(query)
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun retry(city: String) {
        getWeatherData(city)
    }

    fun retryCurrentSearch() {
        getWeatherData(_currentCity.value)
    }
}