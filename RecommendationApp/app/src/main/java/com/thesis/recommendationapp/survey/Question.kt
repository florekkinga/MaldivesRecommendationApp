package com.thesis.recommendationapp.survey

import android.util.Log

class Question(val content: String, val options: List<String>, val jsonFieldName: String, val optionsJson: List<String>, val buttonType: OptionsButtonType, val optionFieldName : String = "options") {
    var answers: MutableList<Boolean> = options.map {false} as MutableList<Boolean>
    var importance: Double = 0.0

    fun generateJSON() : String {
        var json = "\"$jsonFieldName\":{\"$optionFieldName\":"
        if(buttonType == OptionsButtonType.CHECKBOX){
            json += "["
        }
        var separator = ""
        answers.forEachIndexed{ i, element ->
            Log.v("question", element.toString())
            if(element){
                json += separator
                separator = ","
                json += "\"" + optionsJson[i] + "\""
            }
        }
        if(buttonType == OptionsButtonType.CHECKBOX){
            json += "]"
        }
        json += ", \"importance\":$importance}"
        return json
    }
}

enum class OptionsButtonType {
    CHECKBOX,
    RADIO_BUTTON
}