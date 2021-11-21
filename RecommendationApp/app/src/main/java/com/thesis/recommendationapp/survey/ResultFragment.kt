package com.thesis.recommendationapp.survey

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import org.json.JSONObject

class ResultFragment : Fragment() {

    private val viewModel: SurveyViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResortListAdapter
    private var dataSet: Array<ResortDetails> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter = ResortListAdapter(arrayOf(), view.context)
        recyclerView.adapter = adapter
        callRecommendationEngine(view.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun callRecommendationEngine(context: Context) {
        val queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2:8080/recommendation/"
        Log.v("survey-json", viewModel.getJSON())
        val jsonObject = JSONObject(viewModel.getJSON())
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            { response ->
                loadData(response)
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            })
        queue.add(jsonObjectRequest)

    }

    private fun loadData(response: JSONObject) {
        Log.v("loadData", response.toString())
        val tmpData = arrayListOf<ResortDetails>()
        val keys = response.keys()
        while(keys.hasNext()){
            val key : String = keys.next()
            tmpData.add(ResortDetails(key, "", "", response.getString(key)))
        }
        this.dataSet += tmpData
    }

    companion object {
        fun newInstance() = ResultFragment()
    }
}