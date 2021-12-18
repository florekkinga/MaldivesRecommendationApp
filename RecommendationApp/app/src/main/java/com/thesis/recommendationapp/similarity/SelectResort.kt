package com.thesis.recommendationapp.similarity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import org.json.JSONObject

class SelectResort : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var radioGroup: RadioGroup
    private var text = "Cinnamon Dhonveli Maldives"
    private var resorts = mutableListOf<String>("Cinnamon Dhonveli Maldives", "You & Me Maldives", "aaa", "bbbbb")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_resort)
        radioGroup = findViewById(R.id.radioGroup)
        button = findViewById(R.id.findSimilar)
        fetchResorts()
        button.setOnClickListener { getSimilarityResult() }
    }

    private fun createRadioButtons() {
        radioGroup.orientation = LinearLayout.VERTICAL
        var check = true
        resorts.forEach { element ->
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            if(check){
                radioButton.isChecked = true
                check = false;
            }
            radioButton.text = element
            radioGroup.addView(radioButton)
        }
    }

    private fun getSimilarityResult() {
        val radioButton: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
        text = radioButton.text.toString()
        val intent = Intent(this, SimilarityResult::class.java).apply {
            putExtra("resortName", text)
        }
        startActivity(intent)
    }

    private fun fetchResorts() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:8080/resorts/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                loadData(response)
                createRadioButtons()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
            })
        queue.add(jsonObjectRequest)
    }


    private fun loadData(response: JSONObject) {
        val tmpData = arrayListOf<String>()
        val keys = response.keys()
        while(keys.hasNext()){
            val key : String = keys.next()
            tmpData.add(key)
        }
        resorts = tmpData
    }
}