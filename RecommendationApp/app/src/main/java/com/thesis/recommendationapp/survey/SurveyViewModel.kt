package com.thesis.recommendationapp.survey
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.thesis.recommendationapp.R

class SurveyViewModel : ViewModel() {
    private val questions = listOf("Wybierz kategorię resortu:", "pytanie2")
    private var index = 0

    fun resetSurvey() {
        index = 0
    }

    fun getCurrentQuestion() : String {
        return questions[index]
    }

    fun getNumberOfQuestions() : Int {
        return questions.size
    }

    fun getNumberOfCurrentQuestion() : Int {
        return index
    }

    fun pressNext(view: View) : Boolean {
        index += 1
        val shouldUpdate = true
        if (index == questions.size) {
            view.findNavController().navigate(R.id.action_multiselectQuestionFragment_to_resultFragment)
            return !shouldUpdate
        }
        return shouldUpdate
    }

    fun pressPrevious(view: View) : Boolean {
        index -= 1
        val shouldUpdate = true
        return shouldUpdate
    }

}