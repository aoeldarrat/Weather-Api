package com.app.weather

import ApiConfig
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.Room
import com.app.weather.model.AppDatabase
import com.app.weather.navigation.Home
import com.app.weather.navigation.MapPage
import com.app.weather.navigation.Settings
import com.app.weather.navigation.Weather
import com.app.weather.ui.theme.WeatherTheme
import com.app.weather.viewmodels.WeatherViewModel
import com.app.weather.viewmodels.WeatherViewModelFactory
import com.app.weather.views.HomeScreen
import com.app.weather.views.HomeScreenProps
import com.app.weather.views.MapScreen
import com.app.weather.views.SettingsScreen
import com.app.weather.views.WeatherScreen


class MainActivity : ComponentActivity() {

    private val weatherService = ApiConfig.getApiService()
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(weatherService)
    }

    //TODO make a toast manager and display a toast when failing to get location
//    private val alertManager: Toast

    private val LOCATION_PERMISSION_KEY = Manifest.permission.ACCESS_FINE_LOCATION
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            setUserLocationData()
        } else {
            // Handle permission denied
        }
    }

    private val locationListener = LocationListener {
        location -> viewModel.setLatitudeLongitude(location.latitude, location.longitude)
    }

    /**
     * Gets the user location. This step only executes if the user granted permission
     */
    fun setUserLocationData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                10f,
                locationListener,
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun checkLocationPermissionAndSetLocation() {
        when {
            // Permission is already granted so just grab the
            ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION_KEY) == PackageManager.PERMISSION_GRANTED -> {
                setUserLocationData()
            }

            // Show rationale if needed
            shouldShowRequestPermissionRationale(LOCATION_PERMISSION_KEY) -> {
                "Location permissions are needed to get the weather. This wont work otherwise"
                requestPermissionLauncher.launch(LOCATION_PERMISSION_KEY)
            }
            else -> {
                requestPermissionLauncher.launch(LOCATION_PERMISSION_KEY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkLocationPermissionAndSetLocation()

        val roomDatabase = Room.databaseBuilder(
            context = applicationContext,
            klass = AppDatabase::class.java,
            name = "database-name"
        ).build()

        val allUsers = roomDatabase.userDao().getAll()
        val oneUser = roomDatabase.userDao().getUser("user_id")


        setContent {
            WeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        database = roomDatabase
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    database: AppDatabase
) {
    val navController = rememberNavController()
    val pointData by viewModel.pointData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val latitudeSearchQuery by viewModel.latitudeSearchQuery.collectAsState()
    val longitudeSearchQuery by viewModel.longitudeSearchQuery.collectAsState()

    val stationData = viewModel.stationPagedData.collectAsLazyPagingItems()


    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Weather(state = "Good")
    ) {
        composable<Home> {
            HomeScreen(
                // Mock data for now
                props = HomeScreenProps(
                    userLocation = "New York",
                    temperature = 72.0,
                    alerts = listOf("Alert1", "Alert1", "Alert1", "Alert1", "Alert1")
                )
            )
        }
        composable<Weather> {
            WeatherScreen(
                pointData = pointData,
                stationData = stationData,
                isLoading = isLoading,
                error = error,
                searchLatitude = latitudeSearchQuery,
                searchLongitude = longitudeSearchQuery,
                onSearchQueryChange = { lat, long ->
                    viewModel.updateSearchQuery(lat, long)
                },
                onSearchMade = { lat, long ->
                    viewModel.getWeatherData(lat.toDouble(), long.toDouble())
                },
                onNavigateToSettings = {
                    navController.navigate(route = Settings)
                },
                onRetry = {
                    viewModel.retryCurrentSearch()
                }
            )
        }

        composable<Settings> {
            SettingsScreen(onNavigateToWeather = {
                    navController.navigate(
                        route = Weather(state = "Updated")
                    )
                })
        }

        composable<MapPage>{
            MapScreen()
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WeatherTheme {
        MainScreen(
            modifier = Modifier,
            viewModel = WeatherViewModel(ApiConfig.getApiService())
        )
    }
}