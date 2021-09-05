package com.thesis.recommendationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editFeature)
        result = findViewById(R.id.result)

        button.setOnClickListener { callRecommendationEngine() }
    }

    private fun callRecommendationEngine() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://localhost:8080/recommendation/" + editText.text

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            result.text = response
        }, {
            result.text = "error"
        })

        queue.add(stringRequest)
    }
}