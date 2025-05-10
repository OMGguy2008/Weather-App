package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


sealed class State{
    object Loading: State()
    data class Success(val data: DataHandler.WeatherForecastData?): State()
    data class Error(val errorMsg: String): State()
}

class WeatherViewModel: ViewModel() {
    //States
    var weatherState by mutableStateOf<State>(State.Loading)
        private set
    //For searching
    var query by mutableStateOf("")

    init {
        //Searches for data on app launch
        searchWeatherData("Kaunas")
    }

    fun searchWeatherData(q:String = query){
        viewModelScope.launch {
            try {

                //Getting the handler
                val hnd = DataHandler()

                //Making the request
                val result = hnd.getForecast(3, q)
                weatherState = State.Success(result)
            } catch (e: Exception){
                weatherState = State.Error("Failed to get data. Check logs for more info")
                e.printStackTrace()
            }
        }
    }
}

suspend fun getLoc(context: Context):Location = suspendCoroutine { cont ->
    //Getting current device location
    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    val loc: Location

    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        locationClient.lastLocation.addOnSuccessListener { loc ->
            cont.resume(loc)
        }
    }
}



