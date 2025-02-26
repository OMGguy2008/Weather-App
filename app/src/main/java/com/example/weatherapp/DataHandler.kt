package com.example.weatherapp

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class DataHandler {
//Data Classes

    //For realtime data
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
    data class currentWeatherData(
        val location: Location,
        val current: CurrentWeather
    )




    //For weather forecast

    @Serializable
    data class WeatherForecastData(
        val location: Location,
        val current: Current,
        val forecast: Forecast
    )

    @Serializable
    data class Current(
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

    @Serializable
    data class Forecast(
        val forecastday: List<ForecastDay>
    )

    @Serializable
    data class ForecastDay(
        val date: String,
        val date_epoch: Long,
        val day: Day,
        val astro: Astro,
        val hour: List<Hour>
    )

    @Serializable
    data class Day(
        val maxtemp_c: Double,
        val maxtemp_f: Double,
        val mintemp_c: Double,
        val mintemp_f: Double,
        val avgtemp_c: Double,
        val avgtemp_f: Double,
        val maxwind_mph: Double,
        val maxwind_kph: Double,
        val totalprecip_mm: Double,
        val totalprecip_in: Double,
        val totalsnow_cm: Double,
        val avgvis_km: Double,
        val avgvis_miles: Double,
        val avghumidity: Int,
        val daily_will_it_rain: Int,
        val daily_chance_of_rain: Int,
        val daily_will_it_snow: Int,
        val daily_chance_of_snow: Int,
        val condition: Condition,
        val uv: Double
    )

    @Serializable
    data class Astro(
        val sunrise: String,
        val sunset: String,
        val moonrise: String,
        val moonset: String,
        val moon_phase: String,
        val moon_illumination: Int,
        val is_moon_up: Int,
        val is_sun_up: Int
    )

    @Serializable
    data class Hour(
        val time_epoch: Long,
        val time: String,
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
        val snow_cm: Double,
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
        val will_it_rain: Int,
        val chance_of_rain: Int,
        val will_it_snow: Int,
        val chance_of_snow: Int,
        val vis_km: Double,
        val vis_miles: Double,
        val gust_mph: Double,
        val gust_kph: Double,
        val uv: Double
    )




    //Functions
    //Current Weather Data Getter
    suspend fun getCurrentWeatherData(city: String): currentWeatherData? {
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

            //Parsing Json
            val data = Json.decodeFromString<currentWeatherData>(response.bodyAsText())
            return data
        } catch(e: Exception){
            e.printStackTrace()
            return null
        } finally {
            client.close()
        }
    }


    //Weather Forecast Data Getter
    suspend fun getForecast(days: Int, city: String): WeatherForecastData?{
        val client = HttpClient()
        try{
            //Variables
            val url = "http://api.weatherapi.com/v1/forecast.json"
            val key = "95e088be19344374a8d174802242903"

            //Making the request
            val response: HttpResponse = client.get(url){
                url{
                    //Modifiable Data
                    parameters.append("key", key)
                    parameters.append("q", city)
                    parameters.append("days", days.toString())

                    //Constant Data
                    parameters.append("alerts", "no")
                    parameters.append("aqi", "no")
                }
            }
            Log.i("Data", response.status.toString())

            //Parsing the json data
            val data = Json.decodeFromString<WeatherForecastData>(response.bodyAsText())
            return data

        } catch (e: Exception){
            e.printStackTrace()
            return null;
        } finally {
            client.close()
        }
    }
}