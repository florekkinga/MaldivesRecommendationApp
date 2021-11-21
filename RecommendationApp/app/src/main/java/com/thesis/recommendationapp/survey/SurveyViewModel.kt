package com.thesis.recommendationapp.survey
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RatingBar
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.thesis.recommendationapp.R

class SurveyViewModel : ViewModel() {
    private val questions : List<Question> = listOf(
        Question(
            "Select your preferred resort categories:", listOf("5 star", "4 star"),
            "starRating", listOf("5", "4"), OptionsButtonType.CHECKBOX),
        Question(
            "Select your preferred type of transfer from Male to the resort island:",
            listOf("Sea plane", "Speed boat", "Mixed transfer"), "transfer",
            listOf("Sea Plane", "Speed Boat", "Mixed Transfer"), OptionsButtonType.CHECKBOX),
        Question(
            "Choose the maximum transfer price (1 person):", listOf("150$", "300$", "500$"),
            "transferPrice", listOf("150", "300", "500"), OptionsButtonType.RADIO_BUTTON,
            "price"),
        Question(
            "Choose the maximum transfer time:", listOf("30 minutes", "60 minutes", "90 minutes"),
            "transferTime", listOf("30", "60", "90"), OptionsButtonType.RADIO_BUTTON, "time"),
        Question(
            "Select your preferred type of accommodation:", listOf("Room", "Beach bungalow", "Beach bungalow with pool",
            "Water bungalow", "Water bungalow with pool", "Water suite", "Water suite with pool"), "accommodation",
            listOf("Room", "Beach Bungalow", "Beach Bungalow with Pool", "Water Bungalow", "Water Bungalow with Pool",
                "Water Suite", "Water Suite with Pool"), OptionsButtonType.CHECKBOX),
        Question(
            "Select your preferred type of board basis:", listOf("Half board", "Full board", "All inclusive"),
            "boardBasis", listOf("halfBoard", "fullBoard", "allInclusive"), OptionsButtonType.CHECKBOX),
        Question(
            "Select the maximum accommodation price (2 people/ 1 night):", listOf("600$", "1300$", "2000$"),
            "accommodationPrice", listOf("600", "1300", "2000"), OptionsButtonType.RADIO_BUTTON, "price"),
        Question("Select water sports activities that you would like to have available at the resort:",
            listOf("Scuba diving", "Catamaran sailing", "Kite surfing", "Wind surfing", "Snorkeling", "Jet ski", "Motorized sports", "Parasailing", "Fishing", "Kayak", "Stand-up Paddling", "Surfing"),
            "waterSports", listOf("Scuba Diving", "Catamaran Sailing", "Kite Surfing", "Wind Surfing", "Snorkeling", "Jet Ski",
                "Motorized Sports", "Parasailing", "Fishing", "Kayak", "Standup Paddling", "Surfing"), OptionsButtonType.CHECKBOX),
        Question("Select types of wine and dine facilities that you would like to have available at the resort:",
            listOf("Underwater restaurant", "Buffet restaurant", "À la carte restaurant", "Bar", "Pool bar"), "wineAndDine",
            listOf("Underwater Restaurant", "Buffet Restaurant", "À la carte Restaurant", "Bar", "Pool bar"), OptionsButtonType.CHECKBOX),
        Question("Select fitness activities that you would like to have available at the resort:", listOf("Beach volley", "Tennis", "Gym", "Table tennis"),
            "fitness", listOf("Beach Volley", "Tennis", "Gym", "Table Tennis"), OptionsButtonType.CHECKBOX)
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

    fun getTypeOfButtonForOptions() : OptionsButtonType {
        return questions[currQuestionNumber].buttonType
    }

    fun pressNext(view: View) : Boolean {
        val checkBoxes : List<CheckBox> = listOf(
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
        val radioButtons : List<RadioButton> = listOf(
            view.findViewById(R.id.radioButton1),
            view.findViewById(R.id.radioButton2),
            view.findViewById(R.id.radioButton3),
            view.findViewById(R.id.radioButton4)
        )
        val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)

        questions[currQuestionNumber].importance = ratingBar.rating.toDouble()
        questions[currQuestionNumber].answers.forEachIndexed { i, answer ->
            if(getTypeOfButtonForOptions() == OptionsButtonType.CHECKBOX) {
                if (checkBoxes[i].isChecked) {
                    Log.v("survey", questions[currQuestionNumber].jsonFieldName)
                    questions[currQuestionNumber].answers[i] = true
                    Log.v("array-survey", questions[currQuestionNumber].answers[i].toString())
                    Log.v("survey-json", questions[currQuestionNumber].generateJSON())
                }
            }
            else {
                if(radioButtons[i].isChecked) {
                    questions[currQuestionNumber].answers[i] = true
                }
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
        Log.v("survey-json", json)
        return json
    }

}