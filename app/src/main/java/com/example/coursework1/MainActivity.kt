package com.example.coursework1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.com.example.coursework1.WeatherResponse
import com.example.weatherapp.com.example.coursework1.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var layout: RelativeLayout
    private lateinit var cityInput: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherInfo: TextView
    private lateinit var cityName: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var dateTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        layout = findViewById(R.id.main_layout)
        cityInput = findViewById(R.id.city_input)
        getWeatherButton = findViewById(R.id.get_weather_button)
        weatherInfo = findViewById(R.id.weather_info)
        cityName = findViewById(R.id.city_name)
        weatherImage = findViewById(R.id.weather_image)
        dateTime = findViewById(R.id.date_time)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        getWeatherButton.setOnClickListener {
            val city = cityInput.text.toString()
            if (city.isNotEmpty()) {
                getWeather(service, city)
            } else {
                Toast.makeText(this, "Please enter the name of the city.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val aboutButton = findViewById<ImageButton>(R.id.about_button)
        aboutButton.setOnClickListener {
            val weatherCondition = weatherInfo.text.split("\n").let { parts ->
                if (parts.size > 1) {
                    parts[1].substringAfter("Weather: ").trim()
                } else {
                    "Unknown"
                }
            }
            val intent = Intent(this, AutorsActivity::class.java)
            intent.putExtra("weatherCondition", weatherCondition)
            startActivity(intent)
        }
    }

    private fun getWeather(service: WeatherService, city: String) {
        val apiKey = "937c7e3a3a02d3ccba42416631ef7fcf"
        service.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherCondition = response.body()!!.weather[0].main
                    val temperature = response.body()!!.main.temp - 273.15
                    val temperatureText = String.format("%.1f", temperature)
                    weatherInfo.text = "Temperature: $temperatureText°C\nWeather: $weatherCondition"
                    dateTime.text = getCurrentDateTime()
                    cityName.text = cityInput.text.toString()
                    changeBackground(weatherCondition)
                    showWeatherImage(weatherCondition)
                } else {
                    Toast.makeText(this@MainActivity, "Data upload error", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Data upload error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun changeBackground(weatherCondition: String) {
        when (weatherCondition.lowercase()) {
            "clear" -> layout.setBackgroundResource(R.drawable.clear_weather)
            "clouds" -> layout.setBackgroundResource(R.drawable.cloud_weather)
            "rain" -> layout.setBackgroundResource(R.drawable.rain_weather)
            "snow" -> layout.setBackgroundResource(R.drawable.snow_weather)
            else -> layout.setBackgroundResource(R.drawable.clear_weather)
        }
    }

    private fun showWeatherImage(weatherCondition: String) {
        when (weatherCondition.lowercase()) {
            "clear" -> weatherImage.setImageResource(R.drawable.sun)
            "clouds" -> weatherImage.setImageResource(R.drawable.cloud)
            "rain" -> weatherImage.setImageResource(R.drawable.rain)
            "snow" -> weatherImage.setImageResource(R.drawable.snow)
            else -> weatherImage.setImageResource(R.drawable.sun)
        }
    }
}
