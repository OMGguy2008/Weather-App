package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch




sealed class State{
    object Loading: State()
    data class Success(val data: DataHandler.WeatherForecastData?): State()
    data class Error(val errorMsg: String): State()
}

class WeatherViewModel: ViewModel() {
    var weatherState by mutableStateOf<State>(State.Loading)
        private set

    init {

        viewModelScope.launch {
            try {
                //Getting the handler
                val hnd = DataHandler()
                val result = hnd.getForecast(3,"Kaunas")
                weatherState = State.Success(result)
            } catch (e: Exception){
                weatherState = State.Error("Failed to get data. Check logs for more info")
                e.printStackTrace()
            }


        }

    }

}


