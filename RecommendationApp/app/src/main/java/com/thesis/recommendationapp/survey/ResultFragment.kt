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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.thesis.recommendationapp.R

class ResultFragment : Fragment() {
    private lateinit var callRecommendationApiButton: Button
    private lateinit var editText: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        editText = view.findViewById(R.id.editfeature)
        result = view.findViewById(R.id.result)

        callRecommendationApiButton.setOnClickListener { callRecommendationEngine() }
    }

        private fun callRecommendationEngine() {
        val queue = Volley.newRequestQueue(activity)
        // localhost for emulator -> 10.0.2.2
//        val url = "http://10.0.2.2:8080/recommendation/" + editText.text
        val url = "http://10.0.2.2:8080/resorts/"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            result.text = response
        }, { error ->
            result.text = error.toString()
        })

        queue.add(stringRequest)
    }

    companion object {
        fun newInstance() = ResultFragment()
    }
}