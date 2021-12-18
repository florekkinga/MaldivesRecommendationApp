package com.thesis.recommendationapp.resorts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import org.json.JSONArray
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

    private lateinit var bookingButton: Button
    private var bookingUrl = "https://booking.com"

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
        bookingButton = findViewById(R.id.button)
        val resort = intent.getStringExtra("resortName").toString()
        sendRequest(this, resort)
        bookingButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingUrl))
            startActivity(browserIntent)
        }
        actionBar?.setDisplayHomeAsUpEnabled(true)
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
        bookingUrl = response.getString("booking")
        tripAdvisorRating.text = response.getDouble("userRating").toString()
        starRating.text = response.getDouble("starRating").toString()
        accommodation.text = getOptions(response.getJSONArray("accommodation"), "type")
        transfer.text = getOptions(response.getJSONArray("transfer"), "name")
        waterSports.text = getOptions(response.getJSONArray("waterSports"))
        dining.text = getOptions(response.getJSONArray("wineAndDine"))
        fitness.text = getOptions(response.getJSONArray("fitness"))
    }

    private fun getOptions(jsonArray: JSONArray, key: String = "") : String {
        var result = ""
        var prefix = ""
        for(i in 0 until jsonArray.length()){
            result += prefix
            prefix = ", "
            result += if(key == ""){
                jsonArray.getString(i)
            } else {
                jsonArray.getJSONObject(i).getString(key)
            }
        }
        return result
    }
}