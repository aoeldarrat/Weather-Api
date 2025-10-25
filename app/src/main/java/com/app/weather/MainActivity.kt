package com.app.weather

import ApiConfig
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.weather.model.WeatherResponse
import com.app.weather.model.toFahrenheitSafe
import com.app.weather.navigation.Settings
import com.app.weather.navigation.Weather
import com.app.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {

    private val weatherService = ApiConfig.getApiService()
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(weatherService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load initial weather data
        viewModel.getWeatherData(city = "London")

        setContent {
            WeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(
    weather: WeatherResponse?,
    isLoading: Boolean,
    error: String?,
    searchQuery: String,
    currentCity: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Search Weather",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        label = { Text("City name") },
                        placeholder = { Text("e.g., London, Tokyo") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    Button(
                        onClick = onSearch,
                        enabled = !isLoading && searchQuery.isNotBlank()
                    ) {
                        Text("Search")
                    }
                }
            }
        }

        // Weather Content
        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
                Text(
                    text = "Loading weather for $currentCity...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            error != null -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }
            weather != null -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Weather in $currentCity",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Temperature: ${weather.temperature.toFahrenheitSafe()}",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Description: ${weather.description}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Wind: ${weather.wind}",
                            fontSize = 16.sp
                        )
                    }
                }
            }
            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Enter a city name to get weather information",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Settings")
        }
    }
}

@Composable
fun SettingsScreen(
    onNavigateToWeather: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onNavigateToWeather,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Weather")
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel
) {
    val navController = rememberNavController()
    val weatherData by viewModel.weatherData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Weather(state = "Good")
    ) {
        composable<Weather> {
            WeatherScreen(
                weather = weatherData,
                isLoading = isLoading,
                error = error,
                searchQuery = searchQuery,
                currentCity = currentCity,
                onSearchQueryChange = { query ->
                    viewModel.updateSearchQuery(query)
                    viewModel.searchWeather()
                },
                onSearch = {
                    viewModel.searchWeather()
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
            SettingsScreen(
                onNavigateToWeather = {
                    navController.navigate(
                        route = Weather(state = "Updated")
                    )
                }
            )
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