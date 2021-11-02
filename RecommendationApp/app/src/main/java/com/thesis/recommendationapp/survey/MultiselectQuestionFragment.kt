package com.thesis.recommendationapp.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.thesis.recommendationapp.R

class MultiselectQuestionFragment : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: SurveyViewModel
    private lateinit var checkBox3: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)
        return inflater.inflate(R.layout.fragment_multiselect_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        questionTextView = view.findViewById(R.id.question)
        questionNumberTextView = view.findViewById(R.id.questionNumber)
        progressBar = view.findViewById(R.id.progressBar)
        checkBox3 = view.findViewById(R.id.checkBox3)

        updateUI()

        nextButton.setOnClickListener {
            val shouldUpdate = viewModel.pressNext(view)
            if (shouldUpdate) {
                updateUI()
            }
        }

        previousButton.setOnClickListener {
            val shouldUpdate = viewModel.pressPrevious(view)
            if (shouldUpdate) {
                updateUI()
            }
        }

    }

    private fun updateUI() {
        checkBox3.visibility = if (viewModel.getNumberOfCurrentQuestion() == 0) View.GONE else View.VISIBLE
        previousButton.visibility = if (viewModel.getNumberOfCurrentQuestion() == 0) View.GONE else View.VISIBLE
        nextButton.text = if (viewModel.getNumberOfCurrentQuestion() == 1) "ZAKOŃCZ" else "NASTĘPNE"

        questionTextView.text = viewModel.getCurrentQuestion()
        questionNumberTextView.text = getString(
            R.string.questionNumber,
            viewModel.getNumberOfCurrentQuestion() + 1,
            viewModel.getNumberOfQuestions()
        )
        progressBar.progress =
            (viewModel.getNumberOfCurrentQuestion() + 1) * 100 / viewModel.getNumberOfQuestions()
    }

    companion object {
        fun newInstance() = MultiselectQuestionFragment()
    }
}