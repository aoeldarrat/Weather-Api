package com.app.weather

import WeatherService
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.weather.model.PointDataResponse
import com.app.weather.service.StationRepositoryImpl
import com.app.weather.service.StationUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class WeatherViewModel(
    private val weatherService: WeatherService,
) : ViewModel() {

    // Deprecated api logic
//    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
//    val weatherData: StateFlow<WeatherResponse?> = _weatherData.asStateFlow()

    val repository = StationRepositoryImpl(weatherService)
    val useCase = StationUseCase(repository)
    val stationPagedData = useCase.fetchPagedStations()

    private val _pointData = MutableStateFlow<PointDataResponse?>(null)
    val pointData: StateFlow<PointDataResponse?> = _pointData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _latitudeSearchQuery = MutableStateFlow("")
    val latitudeSearchQuery: StateFlow<String> = _latitudeSearchQuery.asStateFlow()

    private val _longitudeSearchQuery = MutableStateFlow("")
    val longitudeSearchQuery: StateFlow<String> = _longitudeSearchQuery.asStateFlow()

    private val _currentLatitude = MutableStateFlow(39.105148)
    private val _currentLongitude = MutableStateFlow(-94.584091)
    val currentLatitude: StateFlow<Double> = _currentLatitude.asStateFlow()
    val currentLongitude: StateFlow<Double> = _currentLongitude.asStateFlow()

    init {

        // Dont need debounce on search for legit locations
//        _latitudeSearchQuery
//            .debounce(500L)
//            .filter { query ->
//                query.isNotBlank()
//            }.onEach {
//                if (!it.isEmpty() && !longitudeSearchQuery.value.isEmpty()) {
//                    getWeatherData(
//                        it.trim().toDouble(),
//                        longitudeSearchQuery.value.trim().toDouble()
//                    )
//                }
//            }.launchIn(viewModelScope)

//        _longitudeSearchQuery
//            .debounce(500L)
//            .filter { query ->
//                query.isNotBlank()
//            }.onEach {
//                if (!it.isEmpty() && !latitudeSearchQuery.value.isEmpty()) {
//                    getWeatherData(
//                        latitudeSearchQuery.value.trim().toDouble(),
//                        it.trim().toDouble()
//                    )
//                }
//            }.launchIn(viewModelScope)
    }

    fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _currentLatitude.value = latitude
                _currentLongitude.value = longitude
                // Deprecated api response code
//                val response = weatherService.getWeather(city)
//                _weatherData.value = response

                val response = weatherService.getPointData(latitude, longitude)
                _pointData.value = response

                Log.d("WeatherViewModel", "Weather data loaded successfully for lat: $latitude, long: $longitude" )
            } catch (e: Exception) {
                val errorMessage = "Failed to load weather data: ${e.message}"
                _error.value = errorMessage
                _pointData.value = null

                Log.e("WeatherViewModel", "Error fetching weather data for lat: $latitude, long: $longitude", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(lat: String?, long: String?) {
        lat?.let {
            _latitudeSearchQuery.value = lat
        }

        long?.let {
            _longitudeSearchQuery.value = long
        }
    }

    fun retryCurrentSearch() {
        getWeatherData(currentLatitude.value, currentLongitude.value)
    }

    fun setLatitudeLongitude(lat: Double, long: Double) {
        _currentLatitude.value = lat
        _currentLongitude.value = long

        retryCurrentSearch()
    }
}