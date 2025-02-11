package com.example.weatherapp

import android.os.Bundle
import android.util.Log;
import android.widget.Button
import android.widget.TextView
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
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.parseClientCookiesHeader
import io.ktor.http.path
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

            //Button
            val btn: Button = findViewById<Button>(R.id.searchbutton)
            btn.setOnClickListener{
                runBlocking {
                    launch {
                        setContentView(R.layout.activity_main)

                        val response: HttpResponse? = getData()
                        val text = findViewById<TextView>(R.id.apioutput)
                        if (response != null) {
                            text.text = response.bodyAsText()
                        } else{
                            text.text = "ERROR"
                        }
                    }
                }
            }

        }
    }
}



//Data
suspend fun getData(): HttpResponse? {
    try {
        //Variables
        val url = "http://api.weatherapi.com/v1/current.json"
        val key = "95e088be19344374a8d174802242903"
        //Networking code
        val client = HttpClient()
        val response: HttpResponse = client.get(url) {
            url {
                parameters.append("key", key)
                parameters.append("q", "Kaunas")
            }
        }
        Log.i("Data", response.status.toString())
        client.close()
        return response
    } catch(e: Exception){
        e.printStackTrace()
        return null
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