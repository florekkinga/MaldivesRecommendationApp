package com.thesis.recommendationapp.survey

class Question(val content: String, val options: List<String>, val jsonFieldName: String, val optionsJson: List<String>, val buttonType: OptionsButtonType, val optionFieldName : String = "options") {
    var answers: MutableList<Boolean> = options.map {false} as MutableList<Boolean>
    var radiobuttonValue: String = "0"
    var importance: Double = 0.0

    fun generateJSON() : String {
        var json = "\"$jsonFieldName\":{\"$optionFieldName\":"
        if(buttonType == OptionsButtonType.CHECKBOX) {
            json += "["
            var separator = ""
            answers.forEachIndexed { i, element ->
                if (element) {
                    json += separator
                    separator = ","
                    json += "\"" + optionsJson[i] + "\""
                }
            }
            json += "]"
        }
        else {
            answers.forEachIndexed{ i, element ->
                if(element){
                    radiobuttonValue = optionsJson[i]
                }
            }
            json += radiobuttonValue
        }
        json += ", \"importance\":$importance}"
        return json
    }
}

enum class OptionsButtonType {
    CHECKBOX,
    RADIO_BUTTON
}