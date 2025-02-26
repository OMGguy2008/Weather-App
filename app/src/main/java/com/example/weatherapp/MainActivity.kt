package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

//Data classes
//Helper data classes
//Location data class
@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Int,
    val localtime: String
)

//Weather data class
@Serializable
data class CurrentWeather(
    val last_updated_epoch: Long,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Int,
    val condition: Condition,
    val wind_mph: Double,
    val wind_kph: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val pressure_mb: Double,
    val pressure_in: Double,
    val precip_mm: Double,
    val precip_in: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val windchill_c: Double,
    val windchill_f: Double,
    val heatindex_c: Double,
    val heatindex_f: Double,
    val dewpoint_c: Double,
    val dewpoint_f: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val uv: Double,
    val gust_mph: Double,
    val gust_kph: Double
)

//Condition decoration data thing
@Serializable
data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)


//Current Weather Class
@Serializable
data class weatherData(
    val location: Location,
    val current: CurrentWeather
)




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "World",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
                runBlocking {
                    launch {
                        val response: HttpResponse? = getData("London")
                        if (response != null) {
                            Log.i("response", response.bodyAsText())
                            //Parsing the Data
                            val data = Json.decodeFromString<weatherData>(response.bodyAsText())
                            Log.i("Data output", "Dabartinė temperatūra: " + data.current.feelslike_c.toString() + " C")
                        } else{
                            Log.e("Error", "Failed to request data")
                            return@launch
                        }
                    }
                }
            }

        }
    }


//Data
suspend fun getData(city: String): HttpResponse? {
    val client = HttpClient()
    try {
        //Variables
        val url = "http://api.weatherapi.com/v1/current.json"
        val key = "95e088be19344374a8d174802242903"
        //Networking code

        val response: HttpResponse = client.get(url) {
            url {
                parameters.append("key", key)
                parameters.append("q", city)
            }
        }
        Log.i("Data", response.status.toString())
        return response
    } catch(e: Exception){
        e.printStackTrace()
        return null
    } finally {
        client.close()
    }
}




//Composables
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Weather App v1",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}