package com.thesis.recommendationapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.survey.ResortDetails
import org.json.JSONObject

class ResortDetailsActivity : AppCompatActivity() {

    private lateinit var resortName: TextView
    private lateinit var addressDetails: TextView
    private lateinit var tripAdvisorRating: TextView
    private lateinit var starRating: TextView
    private lateinit var accommodation: TextView
    private lateinit var transfer: TextView
    private lateinit var dining: TextView
    private lateinit var waterSports: TextView
    private lateinit var fitness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resort_details)
        resortName = findViewById(R.id.resortName)
        addressDetails = findViewById(R.id.resortDetails)
        tripAdvisorRating = findViewById(R.id.tripAdvisorRating)
        starRating = findViewById(R.id.starRating)
        accommodation = findViewById(R.id.accommodation)
        transfer = findViewById(R.id.transfer)
        dining = findViewById(R.id.wineAndDine)
        waterSports = findViewById(R.id.waterSports)
        fitness = findViewById(R.id.fitness)
        val resort = intent.getStringExtra("resortName").toString()
        sendRequest(this, resort)
    }

    private fun sendRequest(context: Context, resortName: String) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2:8080/resortDetails/"
        val jsonObject = JSONObject("{\"name\":\"$resortName\"}")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                loadData(response, resortName)
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            })
        queue.add(jsonObjectRequest)

    }

    private fun loadData(response: JSONObject, resort: String) {
        resortName.text = resort
        addressDetails.text = response.getString("address") + "\n" + response.getString("atol")
        tripAdvisorRating.text = response.getDouble("userRating").toString()
        starRating.text = response.getDouble("starRating").toString()
        accommodation.text = response.getJSONArray("accommodation").toString()
        transfer.text = response.getJSONArray("transfer").toString()
        waterSports.text = response.getJSONArray("waterSports").toString()
        dining.text = response.getJSONArray("wineAndDine").toString()
        fitness.text = response.getJSONArray("fitness").toString()
    }
}