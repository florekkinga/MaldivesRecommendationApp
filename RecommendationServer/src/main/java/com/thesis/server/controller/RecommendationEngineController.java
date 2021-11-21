package com.thesis.server.controller;

import com.thesis.server.engine.RecommendationEngine;
import com.thesis.server.engine.SurveyAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecommendationEngineController {

    private final RecommendationEngine engine;

    @Autowired
    public RecommendationEngineController(RecommendationEngine engine) {
        this.engine = engine;
    }

    @PostMapping(path = "/recommendation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> getResortsNames(@RequestBody SurveyAnswers answers) {

//        List<String> resorts = engine.getRecommendation(answers);
//        Map<String, String> response = new HashMap<>();
//        for(String r : resorts){
//            response.put(r, "0%");
//        }
        return engine.test(answers);
    }
}
