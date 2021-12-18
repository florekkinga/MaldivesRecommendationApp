package com.thesis.recommendationapp.similarity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import com.thesis.recommendationapp.resorts.ResortDetails
import com.thesis.recommendationapp.resorts.ResortListAdapter
import org.json.JSONObject

class SimilarityResult : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResortListAdapter
    private var dataSet: Array<ResortDetails> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similarity_result)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = ResortListAdapter(arrayOf(), this)
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(divider)
        callRecommendationEngine(this, intent.getStringExtra("resortName").toString())
    }

        private fun callRecommendationEngine(context: Context, resortName: String) {
            val queue = Volley.newRequestQueue(context)
            val url = "http://10.0.2.2:8080/similarity/"
            val jsonObject = JSONObject("{\"name\":\"$resortName\"}")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    loadData(response)
                    adapter.dataSet = dataSet
                    adapter.notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                })
            queue.add(jsonObjectRequest)

        }

        private fun loadData(response: JSONObject) {
            Log.v("loadData", response.toString())
            val tmpData = arrayListOf<ResortDetails>()
            val keys = response.keys()
            while(keys.hasNext()){
                val key : String = keys.next()
                val address : String = response.getJSONObject(key).getString("address")
                val booking : String = response.getJSONObject(key).getString("booking")
                val score : Double = response.getJSONObject(key).getDouble("score")
                tmpData.add(ResortDetails(key, address, booking, score))
            }
            tmpData.sortByDescending { it.score }
            this.dataSet += tmpData
            Log.v("dataset", dataSet.toString())
        }
    }