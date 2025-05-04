package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

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

                    mainScreen(WeatherViewModel())
                }
            }
                runBlocking {
                    Log.i("Output", "Dabartinių orų duomenys:")

                    //Get Current Weather
                    launch {
                        val handler = DataHandler()
                        val data = handler.getCurrentWeatherData("Kaunas")
                        //Lil bit of error handling
                        if (data != null) {

                            //Spitting out the data
                            /*
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
                            */

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


// UI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(viewModel: WeatherViewModel){
    when(viewModel.weatherState){
        is State.Loading -> {
            CircularProgressIndicator()
        }
        is State.Success -> {
            val data = (viewModel.weatherState as State.Success).data
            //Background Image
            val backgroundImage = painterResource(R.drawable.cloud_texture)
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()

            )
            //Background
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ){
                //Search bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .padding(32.dp),


                    verticalAlignment = Alignment.CenterVertically

                ){
                    var text = remember { mutableStateOf("") }

                    TextField(
                        value = text.value,
                        onValueChange = {text.value = it},
                        placeholder = {Text("Search For A Location")},
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(236,230,240)
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))

                    )

                }
                Spacer(modifier = Modifier.height(8.dp))

                //Current Status
                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    //Location Text
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = data!!.location.name,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Thin

                    )

                    //Temperature Text
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        text = data.current.temp_c.roundToInt().toString() + "°C",
                        textAlign = TextAlign.Center,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.ExtraLight
                    )

                    //Condition Text
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        text = data.current.condition.text,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Thin
                    )

                }

                Spacer(modifier = Modifier.height(12.dp))

                //Hourly forecast
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(colors = listOf(
                        Color(105,141,244),
                        Color(86,152,240)
                    ))),

                    ) {

                    Text(modifier = Modifier.padding(horizontal = 8.dp),
                        text = "Hourly Forecast",
                        fontSize = 20.sp,
                    )

                    Spacer(modifier = Modifier.height(10.dp).border(1.dp,Color.DarkGray, RectangleShape))

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()),
                    ){
                        HourlyForecastItem("Now", R.drawable.ic_sunny,14)
                        HourlyForecastItem("13:00", R.drawable.ic_sunny, 21)
                        HourlyForecastItem("14:00", R.drawable.ic_sunny, 19)
                        HourlyForecastItem("15:00", R.drawable.ic_sunny, 20)
                        HourlyForecastItem("16:00", R.drawable.ic_sunny, 24)
                        HourlyForecastItem("17:00", R.drawable.ic_sunny, 16)
                        HourlyForecastItem("18:00", R.drawable.ic_sunny, 30)
                        HourlyForecastItem("19:00", R.drawable.ic_sunny, 32)
                        HourlyForecastItem("20:00", R.drawable.ic_sunny, 24)
                        HourlyForecastItem("21:00", R.drawable.ic_sunny, 25)
                        HourlyForecastItem("22:00", R.drawable.ic_sunny, 21)
                        HourlyForecastItem("23:00", R.drawable.ic_sunny, 15)
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))
                //Daily Forecast
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(colors = listOf(
                        Color(105,141,244),
                        Color(86,152,240)
                    )))
                ){
                    Text(
                        text = "Daily Forecast",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                    ) {
                        DailyForecastItem("Today", R.drawable.ic_sunny,R.drawable.ic_sunny,32, 16, 30)
                        DailyForecastItem("Tomorrow", R.drawable.ic_sunny,R.drawable.ic_sunny,22, 10, 83)
                        DailyForecastItem("Wednesday", R.drawable.ic_sunny,R.drawable.ic_sunny,19, 16, 98)
                        DailyForecastItem("Thursday", R.drawable.ic_sunny,R.drawable.ic_sunny,25, 21, 55)
                        DailyForecastItem("Friday", R.drawable.ic_sunny,R.drawable.ic_sunny,25, 21, 55)
                        DailyForecastItem("Saturday", R.drawable.ic_sunny,R.drawable.ic_sunny,25, 21, 55)
                        DailyForecastItem("Sunday", R.drawable.ic_sunny,R.drawable.ic_sunny,25, 21, 55)
                    }
                }
            }
        }

        is State.Error -> {
            Text(text = "Error: ${(viewModel.weatherState as State.Error).errorMsg}")
        }

    }

}

@Composable
fun HourlyForecastItem(time:String, icon:Int , temp:Int){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray),
    ){
        //Time Text
        Text(text = time, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)

        //Icon
        Image(
            modifier = Modifier
                .size(24.dp),
            alignment = Alignment.Center,
            painter = painterResource(icon),
            contentDescription = null

        )

        //Temp Text
        Text(text = "$temp °C", fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun DailyForecastItem(day:String, dayIcon: Int, nightIcon:Int,  maxTemp: Int, minTemp:Int, precipitation: Int){
    Row(modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray),
        ){
        //Day
        Text(modifier = Modifier
            .width(120.dp),
            text = day,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.width(30.dp))

        //Precipitation
        Column(
            modifier = Modifier.fillMaxHeight()
        ){
            //Rain Drop Icon
            Image(painter = painterResource(R.drawable.ic_rain), contentDescription = null, modifier = Modifier.size(24.dp))
            //Number
            Text(modifier = Modifier,
                text = precipitation.toString() + "%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }


        Spacer(modifier = Modifier.width(30.dp))

        //Day Icon
        Image(
            modifier = Modifier
                .size(24.dp),
            alignment = Alignment.Center,
            painter = painterResource(dayIcon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(5.dp))

        //Max Temp
        Text(modifier = Modifier,
            text = maxTemp.toString() + "°C",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.width(20.dp))

        //Night Icon
        Image(
            modifier = Modifier
                .size(24.dp),
            alignment = Alignment.Center,
            painter = painterResource(nightIcon),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(5.dp))

        //Min Temp
        Text(modifier = Modifier,
            text = minTemp.toString() + "°C",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left
        )
    }

}





