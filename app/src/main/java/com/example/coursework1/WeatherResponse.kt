package com.example.weatherapp.com.example.coursework1

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherCondition>
)
data class Main(
    val temp: Double
)
data class WeatherCondition(
    val main: String
)