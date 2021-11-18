package com.thesis.recommendationapp.survey

import android.util.Log

class Question(val content: String, val options: List<String>, val jsonFieldName: String, val optionsJson: List<String>) {
    var answers: MutableList<Boolean> = options.map {false} as MutableList<Boolean>
    var importance: Double = 0.0

    fun generateJSON() : String {
        var json = "\"$jsonFieldName\":{\"options\":["
        var separator = ""
        answers.forEachIndexed{ i, element ->
            Log.v("question", element.toString())
            if(element){
                json += separator
                separator = ","
                json += "\"" + optionsJson[i] + "\""
            }
        }
        json += "], \"importance\":$importance}"
        return json
    }
}