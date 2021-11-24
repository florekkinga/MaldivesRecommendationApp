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
            "Choose the maximum transfer price (1 person):", listOf("200$", "400$", "600$", "600$+"),
            "transferPrice", listOf("200", "400", "600", "1200"), OptionsButtonType.RADIO_BUTTON,
            "price"),
        Question(
            "Choose the maximum transfer time:", listOf("30 minutes", "50 minutes", "70 minutes"),
            "transferTime", listOf("30", "50", "70"), OptionsButtonType.RADIO_BUTTON, "time"),
        Question(
            "Select your preferred type of accommodation:", listOf("Room", "Beach bungalow", "Beach bungalow with pool",
            "Water bungalow", "Water bungalow with pool", "Water suite", "Water suite with pool"), "accommodation",
            listOf("Room", "Beach Bungalow", "Beach Bungalow with Pool", "Water Bungalow", "Water Bungalow with Pool",
                "Water Suite", "Water Suite with Pool"), OptionsButtonType.CHECKBOX),
        Question(
            "Select your preferred type of board basis:", listOf("Half board", "Full board", "All inclusive"),
            "boardBasis", listOf("halfBoard", "fullBoard", "allInclusive"), OptionsButtonType.CHECKBOX),
        Question(
            "Select the maximum accommodation price (2 people/ 1 night):", listOf("800$", "1500$", "2000$", "2000$+"),
            "accommodationPrice", listOf("800", "1500", "2000", "7000"), OptionsButtonType.RADIO_BUTTON, "price"),
        Question("Select water sports activities that you would like to have available at the resort:",
            listOf("Scuba diving", "Catamaran sailing", "Kite surfing", "Wind surfing", "Snorkeling", "Jet ski", "Parasailing", "Kayak", "Stand-up Paddling", "Surfing"),
            "waterSports", listOf("Scuba Diving", "Catamaran Sailing", "Kite Surfing", "Wind Surfing", "Snorkeling", "Jet Ski", "Parasailing", "Kayak", "Standup Paddling", "Surfing"), OptionsButtonType.CHECKBOX),
        Question("Select types of wine and dine facilities that you would like to have available at the resort:",
            listOf("Underwater restaurant", "Buffet restaurant", "À la carte restaurant", "Bar", "Pool bar"), "wineAndDine",
            listOf("Underwater Restaurant", "Buffet Restaurant", "À la carte Restaurant", "Bar", "Pool Bar"), OptionsButtonType.CHECKBOX),
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
                questions[currQuestionNumber].answers[i] = checkBoxes[i].isChecked
            }
            else {
                questions[currQuestionNumber].answers[i] = radioButtons[i].isChecked
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