package com.example.weatherapp.com.example.coursework1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("weather")
    fun getWeather(@Query("q") city: String, @Query("appid") apiKey: String): Call<WeatherResponse>
}