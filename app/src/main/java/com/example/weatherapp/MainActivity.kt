package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme{
                mainScreen(WeatherViewModel())
            }
        }
    }
}
//Composables
// UI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainScreen(viewModel: WeatherViewModel){
    viewModel.searchWeatherData("Kaunas")
    when(viewModel.weatherState){
        is State.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
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
                Spacer(modifier = Modifier.height(8.dp))
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
                        value = viewModel.query,
                        onValueChange = { viewModel.query = it },
                        placeholder = { Text("Search For A Location", color = Color.Gray) },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(210, 210, 210, 255),
                            focusedTextColor = Color(44, 44, 44, 255),
                            focusedIndicatorColor = Color.DarkGray,
                            cursorColor = Color.DarkGray,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        keyboardActions = KeyboardActions(onSearch = { viewModel.searchWeatherData() }),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
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
                        fontWeight = FontWeight.Thin,
                        color = Color.White


                    )

                    //Temperature Text
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        text = data.current.temp_c.roundToInt().toString() + "°C",
                        textAlign = TextAlign.Center,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color.White
                    )

                    //Condition Text
                    Text(modifier = Modifier
                        .fillMaxWidth(),
                        text = data.current.condition.text,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.White
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
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp).border(1.dp,Color.DarkGray, RectangleShape))

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState()),
                    ){
                        for (hour in data!!.forecast.forecastday[0].hour){
                            HourlyForecastItem(hour.time, hour.condition.icon, hour.temp_c.roundToInt())
                        }

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
                        color = Color.White
                    )
                    Column(modifier = Modifier
                        .verticalScroll(rememberScrollState())
                    ) {
                        for(day in data!!.forecast.forecastday){
                            //Special cases
                            if(day == data!!.forecast.forecastday[0]){
                                DailyForecastItem("Today", day.day.condition.icon,day.day.condition.icon, day.day.maxtemp_c.roundToInt(), day.day.mintemp_c.roundToInt(), day.day.daily_chance_of_rain)
                                continue
                            }
                            if(day == data!!.forecast.forecastday[1]){
                                DailyForecastItem("Tomorrow", day.day.condition.icon,day.day.condition.icon, day.day.maxtemp_c.roundToInt(), day.day.mintemp_c.roundToInt(), day.day.daily_chance_of_rain)
                                continue
                            }
                            DailyForecastItem(day.date, day.day.condition.icon,day.day.condition.icon, day.day.maxtemp_c.roundToInt(), day.day.mintemp_c.roundToInt(), day.day.daily_chance_of_rain)
                        }

                    }
                }
            }
        }

        is State.Error -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(colors = listOf(Color(105,141,244), Color(86,152,240)))),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 150.dp)
                ){
                    Text(text = "Error: ${(viewModel.weatherState as State.Error).errorMsg}",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {viewModel.searchWeatherData("Kaunas")},
                        modifier = Modifier.size(200.dp).wrapContentSize(),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.DarkGray),
                        border = BorderStroke(2.dp, color = Color.Black)

                    ) {
                        Text(
                            text = "Retry",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,

                        )
                    }
                }
            }

            }

        }
    }


@Composable
fun HourlyForecastItem(time:String, icon:String, temp: Int){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray),
    ){
        //Time Text
        Text(
            text = if(time == "Now"){"Now"} else {time.substring(startIndex = 10)},
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        //Icon
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data("https:${icon}").crossfade(false).build(),
            modifier = Modifier
            .size(50.dp),
            alignment = Alignment.Center,
            contentDescription = null
        )
        //Temp Text
        Text(text = "$temp °C", fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.White)
    }
}

@Composable
fun DailyForecastItem(day:String, dayIcon: String, nightIcon: String, maxTemp: Int, minTemp:Int, precipitation: Int){
    Row(modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray),
        ){
        //Day
        Text(modifier = Modifier
            .width(120.dp),
            text = if(day == "Today"){"Today"} else if(day == "Tomorrow"){"Tomorrow"} else{day.substring(startIndex = 5)},
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            color = Color.White

        )

        Spacer(modifier = Modifier.width(15.dp))

        //Precipitation
        Column(
            modifier = Modifier.fillMaxHeight()
        ){
            //Rain Drop Icon
            Image(painter = painterResource(R.drawable.ic_raindrop), contentDescription = null, modifier = Modifier.size(24.dp))
            //Number
            Text(modifier = Modifier,
                text = precipitation.toString() + "%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.width(30.dp))

        //Day Icon
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data("https:${dayIcon}").crossfade(false).build(),
            modifier = Modifier
                .size(40.dp),
            alignment = Alignment.Center,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(5.dp))

        //Max Temp
        Text(modifier = Modifier,
            text = maxTemp.toString() + "°C",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(20.dp))

        //Night Icon
        AsyncImage(
            modifier = Modifier
                .size(40.dp),
            alignment = Alignment.Center,
            model = ImageRequest.Builder(context = LocalContext.current).data("https:${nightIcon}").crossfade(false).build(),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(5.dp))

        //Min Temp
        Text(modifier = Modifier,
            text = minTemp.toString() + "°C",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
            color = Color.White
        )
    }

}