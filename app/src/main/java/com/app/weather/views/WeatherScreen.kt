package com.app.weather.views

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.app.weather.model.PointDataResponse
import com.app.weather.model.StationFeature

@Composable
fun WeatherScreen(
    pointData: PointDataResponse?,
    stationData: LazyPagingItems<StationFeature>,
    isLoading: Boolean,
    error: String?,
    searchLatitude: String,
    searchLongitude: String,
    onSearchQueryChange: (String?, String?) -> Unit,
    onSearchMade: (String, String) -> Unit,
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
                        value = searchLatitude,
                        onValueChange = {
                            onSearchQueryChange(it, null)
                        },
                        label = { Text("Latitude") },
                        placeholder = { Text("e.g. -1.532548") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = searchLongitude,
                        onValueChange = {
                            onSearchQueryChange(null, it)
                        },
                        label = { Text("longitude") },
                        placeholder = { Text("e.g. -78.341774") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = {
                            onSearchMade(searchLatitude, searchLongitude)
                        }
                    ) {
                        Text(text = "Search")
                    }

                    for(data in stationData.itemSnapshotList.items) {
                        Text(text = data.properties.name)
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
                    text = "Loading weather for $searchLatitude, $searchLongitude ...",
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
            pointData != null -> {
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
                            text = "Weather in $searchLatitude, $searchLongitude",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Fire weather zone url: ${pointData.properties.timeZone}",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
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
            Text("Go to Settings page")
        }
    }
}