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
@Serializable
data class currentWeatherForecast(
    val last_updated: String
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
                            val data = Json{ignoreUnknownKeys = true}.decodeFromString<currentWeatherForecast>(response.body())
                            Log.i("Data", "Data from: " + data.last_updated)
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