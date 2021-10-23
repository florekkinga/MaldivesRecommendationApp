package com.thesis.recommendationapp.recommendation

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thesis.recommendationapp.R
import com.thesis.recommendationapp.SurveyActivity

class NewRecommendationFragment : Fragment() {

    companion object {
        fun newInstance() = NewRecommendationFragment()
    }

    private lateinit var viewModel: NewRecommendationViewModel
    private lateinit var startRecommendationEngineButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(NewRecommendationViewModel::class.java)
        return inflater.inflate(R.layout.new_recommendation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startRecommendationEngineButton = view.findViewById(R.id.startRecommendationEngineButton)
        startRecommendationEngineButton.setOnClickListener { openSurveyActivity() }
    }

    private fun openSurveyActivity() {
        val intent = Intent(activity, SurveyActivity::class.java).apply {}
        startActivity(intent)
    }

}