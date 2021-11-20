package com.thesis.recommendationapp.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R
import org.json.JSONArray
import org.json.JSONObject

class ResultFragment : Fragment() {
    private lateinit var callRecommendationApiButton: Button
    private lateinit var result: TextView

    private val viewModel: SurveyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callRecommendationApiButton = view.findViewById(R.id.callapi)
        result = view.findViewById(R.id.result)

        callRecommendationApiButton.setOnClickListener { callRecommendationEngine() }
    }

    private fun callRecommendationEngine() {
        val queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2:8080/recommendation/" // localhost for emulator -> 10.0.2.2
        val jsonObject =
            JSONObject("""{"starRating":{"options":["4", "6"], "importance":"6.0"}}, {"transfer":{"options":["a","b"],"importance":"5.0"}},{"accommodation":{"options":["c","d"],"importance":"5.0"}},{"transferPrice":{"price":"5","importance":"5.0"}},{"transferTime":{"time":10,"importance":"5.0"}}""")

        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.POST, url, jsonObject, { response ->
                result.text = response.toString()
            }, { error ->
                result.text = error.toString()
            })
        queue.add(jsonObjectRequest)

    }

    companion object {
        fun newInstance() = ResultFragment()
    }
}