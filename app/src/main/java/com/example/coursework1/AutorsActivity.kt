package com.example.coursework1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AutorsActivity : AppCompatActivity() {
    private lateinit var layout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.autors_layout)

        layout = findViewById(R.id.about_layout)

        val weatherCondition = intent.getStringExtra("weatherCondition") ?: "clear"

        changeBackground(weatherCondition)

        val exitButton = findViewById<ImageButton>(R.id.exit_button)
        exitButton.setOnClickListener {
            finish()
        }

        val authorsTextView = findViewById<TextView>(R.id.authors_text)
        authorsTextView.text = "The authors of the application:\n" +
                "\n" +
                "1. Daniil Romanovsky\n" +
                "2. Artem Gulyaev\n" +
                "3. Nikita Kuzminov"
    }

    private fun changeBackground(weatherCondition: String) {
        when (weatherCondition.lowercase()) {
            "clear" -> layout.setBackgroundResource(R.drawable.clear_weather)
            "clouds" -> layout.setBackgroundResource(R.drawable.cloud_weather)
            "rain" -> layout.setBackgroundResource(R.drawable.rain_weather)
            "snow" -> layout.setBackgroundResource(R.drawable.snow_weather)
            else -> layout.setBackgroundResource(R.drawable.sun)
        }
    }
}