package com.thesis.recommendationapp.survey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.thesis.recommendationapp.R

class MultiselectQuestionFragment : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var checkBoxes: List<CheckBox>
    private lateinit var ratingBar: RatingBar

    private val viewModel: SurveyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v("a", "loooooooooooooooooool")
//        viewModel = ViewModelProvider(this).get(SurveyViewModel::class.java)
        return inflater.inflate(R.layout.fragment_multiselect_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)
        questionTextView = view.findViewById(R.id.question)
        questionNumberTextView = view.findViewById(R.id.questionNumber)
        progressBar = view.findViewById(R.id.progressBar)
        ratingBar = view.findViewById(R.id.ratingBar)
        checkBoxes = listOf(
            view.findViewById(R.id.checkBox1),
            view.findViewById(R.id.checkBox2),
            view.findViewById(R.id.checkBox3),
            view.findViewById(R.id.checkBox4),
            view.findViewById(R.id.checkBox5),
            view.findViewById(R.id.checkBox6)
        )

        updateUI()

        nextButton.setOnClickListener {
            checkBoxes.forEach {c -> Log.v("c", c.isChecked.toString())}
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
        setUpQuestion()
        setUpAnswers()
        setUpPrevAndNextButtons()
        resetCheckboxes()
        resetRatingBar()
    }

    private fun resetRatingBar() {
        ratingBar.rating = 0F
    }

    private fun resetCheckboxes() {
        checkBoxes.forEach { c ->
            c.isChecked = false
        }
    }

    private fun setUpPrevAndNextButtons() {
        previousButton.visibility =
            if (viewModel.getNumberOfCurrentQuestion() == 0) View.GONE else View.VISIBLE
        nextButton.text = if (viewModel.getNumberOfCurrentQuestion() == viewModel.getNumberOfQuestions() - 1) "ZAKOŃCZ" else "NASTĘPNE"

    }

    private fun setUpAnswers() {
        val options = viewModel.getCurrentOptions()
        val numberOfOptions = options.size

        checkBoxes.forEachIndexed { i, element ->
            if(i < numberOfOptions) {
                element.text = options[i]
                element.visibility = View.VISIBLE
            }
            else {
                element.visibility = View.GONE
            }
        }

    }

    private fun setUpQuestion() {
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