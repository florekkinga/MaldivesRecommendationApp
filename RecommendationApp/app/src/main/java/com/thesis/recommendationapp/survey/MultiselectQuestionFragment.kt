package com.thesis.recommendationapp.survey

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.forEachIndexed
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
    private lateinit var radioButtonGroup: RadioGroup
    private lateinit var radioButtons: List<RadioButton>
    private lateinit var ratingBar: RatingBar

    private val viewModel: SurveyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        radioButtonGroup = view.findViewById(R.id.radioGroup)
        checkBoxes = listOf(
            view.findViewById(R.id.checkBox1),
            view.findViewById(R.id.checkBox2),
            view.findViewById(R.id.checkBox3),
            view.findViewById(R.id.checkBox4),
            view.findViewById(R.id.checkBox5),
            view.findViewById(R.id.checkBox6),
            view.findViewById(R.id.checkBox7),
            view.findViewById(R.id.checkBox8),
            view.findViewById(R.id.checkBox9),
            view.findViewById(R.id.checkBox10),
            view.findViewById(R.id.checkBox11),
            view.findViewById(R.id.checkBox12),
            view.findViewById(R.id.checkBox13)
        )
        radioButtons = listOf(
            view.findViewById(R.id.radioButton1),
            view.findViewById(R.id.radioButton2),
            view.findViewById(R.id.radioButton3),
            view.findViewById(R.id.radioButton4),
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
        setUpRatingBar()
    }

    private fun setUpRatingBar() {
        ratingBar.rating = viewModel.getCurrentImportance().toFloat()
    }

    private fun setUpPrevAndNextButtons() {
        previousButton.visibility =
            if (viewModel.getNumberOfCurrentQuestion() == 0) View.GONE else View.VISIBLE
        nextButton.text = if (viewModel.getNumberOfCurrentQuestion() == viewModel.getNumberOfQuestions() - 1) "FINISH" else "NEXT"

    }

    private fun setUpAnswers() {
        val options = viewModel.getCurrentOptions()
        val answers = viewModel.getCurrentAnswers()
        val buttonType = viewModel.getTypeOfButtonForOptions()
        val numberOfOptions = options.size

        if(buttonType == OptionsButtonType.CHECKBOX) {
            radioButtonGroup.visibility = View.GONE
            checkBoxes.forEach{ c -> c.visibility = View.VISIBLE }
            checkBoxes.forEachIndexed { i, element ->
                if(i < numberOfOptions) {
                    element.text = options[i]
                    element.isChecked = answers[i]
                }
                else {
                    element.visibility = View.GONE
                }
            }
        }
        else {
            checkBoxes.forEach{ c -> c.visibility = View.GONE }
            radioButtonGroup.visibility = View.VISIBLE
            radioButtonGroup.clearCheck()
            radioButtons.forEach{ c -> c.visibility = View.VISIBLE }
            radioButtons.forEachIndexed { i, element ->
                if(i < numberOfOptions) {
                    element.text = options[i]
                    Log.v("radioB", answers[i].toString())
                    element.isChecked = answers[i]
                    Log.v("radioB", element.text.toString() + element.isChecked.toString())
                }
                else {
                    element.visibility = View.GONE
                }
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