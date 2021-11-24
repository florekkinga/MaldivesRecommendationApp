package com.thesis.recommendationapp.similarity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.thesis.recommendationapp.R
import com.thesis.recommendationapp.SurveyActivity

class SelectResort : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_resort)

        button = findViewById(R.id.findSimilar)
        button.setOnClickListener {
            val intent = Intent(this, SimilarityResult::class.java).apply {}
            startActivity(intent)
        }
    }
}