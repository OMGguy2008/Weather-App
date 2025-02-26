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
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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
                    Log.i("Output", "Dabartinių orų duomenys:")

                    //Get Current Weather
                    launch {
                        val handler = DataHandler()
                        val data = handler.getCurrentWeatherData("London")
                        //Lil bit of error handling
                        if (data != null) {

                            //Spitting out the data
                            Log.i("Output", "Miestas: " + data.location.name) //Miestas
                            Log.i("Output", "Laikas: " + data.location.localtime) //Laikas

                            Log.i("Output", "Oro Temperatūra: " + data.current.temp_c + " C" ) //Oro Temperatūrą
                            Log.i("Output", "Jutiminė temperatūra: " + data.current.feelslike_c + " C") //Jutiminė Temperatūra

                            Log.i("Output", "Orų būsena: " + data.current.condition.text) //Orų būsena

                            Log.i("Output", "Vėjo kryptis: " + data.current.wind_dir) //Vėjo kryptis
                            Log.i("Output", "Vėjo greitis: " + data.current.wind_kph + "km/h") //Vėjo greitis

                            Log.i("Output", "Vėjo gūsiai: " + data.current.gust_kph + "km/h") //Vėjo gūsiai

                            Log.i("Output", "Oro Drėgnumas: " + data.current.humidity + "%") //Oro Drėgnumas

                            Log.i("Output", "Matomumas: " + data.current.vis_km + "km") //Matomumas

                            Log.i("Output", "Ultravioletinės Spinduliuotės Lygis: " + data.current.uv) //Ultravioletinės Spinduliuotės Lygis

                            Log.i("Output", "-----------------------------------------------------------------------------------------")

                            Log.i("Output", "Ateities orų duomenys:")
                        } else{
                            Log.e("Error", "Failed to request data")
                            return@launch
                        }
                    }


                    //Get weather forecast
                    launch{
                        //Sending the request and getting the data
                        val handler = DataHandler()
                        val data: DataHandler.WeatherForecastData? = handler.getForecast(3, "London")

                        //Lil bit of error handling
                        if (data != null) {
                            Log.i("Output", "Miestas: " + data.location.name) // Miestas

                            for (day in data.forecast.forecastday){
                                Log.i("Output", "Diena: " + day.date) //Data

                                Log.i("Output", "Didžiausia Temperatūra: " + day.day.maxtemp_c + " C") // Max Temp
                                Log.i("Output", "Vidutinė Temperatūra: " + day.day.avgtemp_c + " C") // Vid Temp

                                Log.i("Output", "Didžiausias vėjo greitis" + day.day.maxwind_kph + " km/h") // Max Vėjas

                                Log.i("Output", "Vidutinis Matomumas: " + day.day.avgvis_km + " km") // Vidutinis matomumas

                                Log.i("Output", "Oro būsena: " + day.day.condition.text) // Oro būsena

                                Log.i("Output", "Vidutinis drėgnumas: " + day.day.avghumidity + "%") // Oro būsena

                                Log.i("Output", "Ultravioletinės spinduliuotės lygis: " + day.day.uv) //Ultravioletinės spinduliuotės lygis

                                Log.i("Output", "--------------------------------------------------------------------------------------")

                            }

                        } else{
                            Log.e("Error", "Failed to request data")
                            return@launch
                        }
                    }
                }
            }

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