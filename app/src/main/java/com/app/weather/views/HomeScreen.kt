package com.app.weather.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.app.weather.utilities.Dp


data class HomeScreenProps(
    val userLocation: String,
    val temperature: Double,
    val alerts: List<String>,
)

@Composable
fun HomeScreen(props: HomeScreenProps) {
    Column {
        Column(
            modifier = Modifier.padding(Dp.standard),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Temperature",
                style = TextStyle.Default
            )

            Text(
                text = "${props.temperature}",
                style = TextStyle.Default
            )


        }

        // temperature

        // Widget for alerts

    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        props = HomeScreenProps(
            userLocation = "New York",
            temperature = 72.0,
            alerts = listOf("Alert1", "Alert1", "Alert1", "Alert1", "Alert1")
        )
    )
}