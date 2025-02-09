package com.example.weatherapp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class Data {
    val requestUrl = "https://ktor.io/"
    val key = ""

    suspend fun getData(){
        //Creating the http client
        val client = HttpClient()
        //Fetching
        val response: HttpResponse = client.get(requestUrl)
        //Spitting out the response
        println(response.status)
    }
}