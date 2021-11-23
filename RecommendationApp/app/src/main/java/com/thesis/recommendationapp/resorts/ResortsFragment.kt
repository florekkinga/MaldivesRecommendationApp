package com.thesis.recommendationapp.resorts

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import com.thesis.recommendationapp.survey.ResortDetails
import com.thesis.recommendationapp.survey.ResortListAdapter
import org.json.JSONObject

class ResortsFragment : Fragment() {

    companion object {
        fun newInstance() = ResortsFragment()
    }

    private lateinit var viewModel: ResortsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResortListAdapter
    private var dataSet: Array<ResortDetails> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ResortsViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_result, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager
        adapter = ResortListAdapter(arrayOf(),
            view.context)
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(divider)
        callRecommendationEngine(view.context)
        return view
    }

    private fun callRecommendationEngine(context: Context) {
        val queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2:8080/resorts/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                loadData(response)
                adapter.dataSet = dataSet
                adapter.notifyDataSetChanged()
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
            val address : String = response.getJSONObject(key).getString("address")
            val booking : String = response.getJSONObject(key).getString("booking")
            val rating : Double = response.getJSONObject(key).getDouble("userRating")
            tmpData.add(ResortDetails(key, address, booking, rating, ""))
        }
        tmpData.sortByDescending { it.score }
        this.dataSet += tmpData
        Log.v("dataset", dataSet.toString())
    }

}