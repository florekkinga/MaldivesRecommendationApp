package com.thesis.recommendationapp.survey
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.RatingBar
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.thesis.recommendationapp.R

class SurveyViewModel : ViewModel() {
    private val questions : List<Question> = listOf(
        Question("Jakie kategorie resortu najbardziej Cię interesują?", listOf("5 gwiazdek", "4 gwiazdki"), "starRating", listOf("5", "4")),
        Question("Jakie rodzaje transferu z Male na docelową wyspę bierzesz pod uwagę?", listOf("Wodolot", "Szybka łódka", "Inny/ mieszany transfer"), "transfer", listOf("seaPlane", "speedBoat", "mixedTransfer")),
        Question("Wybierz preferowaną cenę transferu z Male na docelową wyspę:", listOf("<150$", "<300$", "<500$"), "transferPrice", listOf("150", "300", "500")),
        Question("Wybierz preferowany czas transferu z Male na docelową wyspę (dla 1 osoby):", listOf("<30 min", "<60 min", "<90 min"), "transferTime", listOf("30", "60", "90")),
        Question("Jakie rodzaje zakwaterowania bierzesz pod uwagę?", listOf("Pokój", "Bungalow na plaży", "Bungalow na plaży z basenem"), "accommodation", listOf("room", "beachBungalow", "beachBungalowPool")),
        Question("Wybierz preferowaną cenę zakwaterowania (dla 2 osób/ 1 noc):", listOf("<600$", "<1300$", "<2000$"), "accomodationPrice", listOf("600", "1300", "2000"))
    )
    private var currQuestionNumber = 0

    fun resetSurvey() {
        currQuestionNumber = 0
    }

    fun getCurrentQuestion() : String {
        return questions[currQuestionNumber].content
    }

    fun getNumberOfQuestions() : Int {
        return questions.size
    }

    fun getCurrentOptions() : List<String> {
        return questions[currQuestionNumber].options
    }

    fun getNumberOfCurrentQuestion() : Int {
        return currQuestionNumber
    }

    fun pressNext(view: View) : Boolean {
        val checkBoxes : List<CheckBox> = listOf(
            view.findViewById(R.id.checkBox1),
            view.findViewById(R.id.checkBox2),
            view.findViewById(R.id.checkBox3)
        )
        val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)

        questions[currQuestionNumber].importance = ratingBar.rating.toDouble()
        questions[currQuestionNumber].answers.forEachIndexed { i, answer ->
            if(checkBoxes[i].isChecked) {
                Log.v("survey", questions[currQuestionNumber].jsonFieldName)
                questions[currQuestionNumber].answers[i] = true
                Log.v("array-survey", questions[currQuestionNumber].answers[i].toString())
                Log.v("survey-json", questions[currQuestionNumber].generateJSON())
            }
        }

        if(currQuestionNumber == 1){
            Log.v("survey-json", questions[currQuestionNumber-1].generateJSON())
        }

        val shouldUpdate = true
        currQuestionNumber += 1
        if (currQuestionNumber == questions.size) {
            view.findNavController().navigate(R.id.action_multiselectQuestionFragment_to_resultFragment)
            return !shouldUpdate
        }
        return shouldUpdate
    }

    fun pressPrevious(view: View) : Boolean {
        currQuestionNumber -= 1
        val shouldUpdate = true
        return shouldUpdate
    }

    fun getJSON() : String {
        var json = "{"
        var separator = ""
        Log.v("survey-json", questions[0].generateJSON())
        Log.v("survey-json", questions[1].generateJSON())
        questions.forEach{ question ->
            json += separator
            separator = ","
            json += question.generateJSON()
        }
        json += "}"
        return json
    }

}