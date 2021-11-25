package com.thesis.recommendationapp.similarity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.thesis.recommendationapp.R
import com.thesis.recommendationapp.SurveyActivity

class SelectResort : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var inputResort: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_resort)

        inputResort = findViewById(R.id.inputResort)
        button = findViewById(R.id.findSimilar)
        button.setOnClickListener {
            if(inputResort.text != ""){
                val intent = Intent(this, SimilarityResult::class.java).apply {
                    putExtra("resortName", inputResort.text.toString())
                }
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_LONG).show();
            }

        }
    }
}